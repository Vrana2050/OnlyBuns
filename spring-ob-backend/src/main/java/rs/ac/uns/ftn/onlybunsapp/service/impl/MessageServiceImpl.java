package rs.ac.uns.ftn.onlybunsapp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rs.ac.uns.ftn.onlybunsapp.dto.MessageDto;
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
    @Scheduled(fixedRate = 5000)
    public void fetchMessagesPeriodically() {
        String url = brokerUrl + "/api/messages";
        //System.out.println("Luka Vrana " + url);
        try {
            ResponseEntity<MessageDto> response = restTemplate.getForEntity(url, MessageDto.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                MessageDto message = response.getBody();
                RabbitCareObject rabbitCareObject = new RabbitCareObject();
                rabbitCareObject.setName(message.getName());
                rabbitCareObject.setLongitude(message.getLocation().getLongitude());
                rabbitCareObject.setLatitude(message.getLocation().getLatitude());

                rabbitCareObjectRepository.save(rabbitCareObject);

                System.out.println("Objekat za brigu o zecevima sacuvan.");

            } else if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
                System.out.println("Nema novih poruka.");
            }
        } catch (Exception e) {
            System.err.println("Greška prilikom preuzimanja poruka: " + e.getMessage());
        }
    }

}
