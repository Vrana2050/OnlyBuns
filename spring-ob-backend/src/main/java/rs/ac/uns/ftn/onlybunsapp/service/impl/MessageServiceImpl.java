package rs.ac.uns.ftn.onlybunsapp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import rs.ac.uns.ftn.onlybunsapp.dto.LocationMessageDto;
import rs.ac.uns.ftn.onlybunsapp.model.RabbitCareObject;
import rs.ac.uns.ftn.onlybunsapp.repository.RabbitCareObjectRepository;
import rs.ac.uns.ftn.onlybunsapp.service.MessageService;

import javax.annotation.PostConstruct;

@Service
public class MessageServiceImpl implements MessageService {

    private RestTemplate restTemplate;

    @Autowired
    private RabbitCareObjectRepository rabbitCareObjectRepository;

    @Value("${broker.url}")
    private String brokerUrl;

    @PostConstruct
    public void initializeRestTemplate() {
        restTemplate = new RestTemplate();
    }


    @Override
    @Scheduled(fixedRate = 15000)
    public void fetchMessagesPeriodically() {
        String url = brokerUrl + "/broker/consume/mq";
        //System.out.println("Luka Vrana " + url);
        try {
            ResponseEntity<LocationMessageDto> response = restTemplate.getForEntity(url, LocationMessageDto.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                LocationMessageDto message = response.getBody();
                RabbitCareObject rabbitCareObject = new RabbitCareObject();
                rabbitCareObject.setName(message.getName());
                rabbitCareObject.setLongitude(message.getLocation().getLongitude());
                rabbitCareObject.setLatitude(message.getLocation().getLatitude());

                rabbitCareObjectRepository.save(rabbitCareObject);

                System.out.println("Objekat za brigu o zecevima sacuvan.");

                String ackUrl = brokerUrl + "/broker/ack?queue=mq&messageId=" + message.getId();
                ResponseEntity<Void> ackResponse = restTemplate.postForEntity(ackUrl, null, Void.class);

                if (ackResponse.getStatusCode().is2xxSuccessful()) {
                    System.out.println("Poruka uspešno potvrđena (ACK).");
                } else {
                    System.err.println("Neuspešan ACK: " + ackResponse.getStatusCode());
                }
            } else if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
                System.out.println("Nema novih poruka.");
            }
        } catch (ResourceAccessException e) {
            // Ovo obuhvata ConnectException, SocketTimeoutException itd.
            System.err.println("Broker nije dostupan: " + e.getMessage());
        }catch (Exception e) {
            System.err.println("Greška prilikom preuzimanja poruka: " + e.getMessage());
        }
    }

}
