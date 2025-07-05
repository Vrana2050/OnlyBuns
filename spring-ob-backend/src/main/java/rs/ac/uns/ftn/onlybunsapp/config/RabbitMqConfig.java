package rs.ac.uns.ftn.onlybunsapp.config;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Value("${myqueue}")
    String queue;

    @Value("${myqueue2}")
    String queue2;
    @Value("${myqueue3}")
    String queue3;

    @Value("${myexchange}")
    String exchange;
    @Value("${fanoutExchange}")
    String FANOUT_EXCHANGE;


    @Value("${routingkey}")
    String routingkey;


    @Bean
    Queue queue() {
        return new Queue(queue, true);
    }

    @Bean
    Queue queue2() {
        return new Queue(queue2, true);
    }
    @Bean
    Queue queue3() {
        return new Queue(queue3, true);
    }

    @Bean
    DirectExchange exchange() {
        return new DirectExchange(exchange);
    }
    @Bean
    FanoutExchange fanoutExchange() {
        return new FanoutExchange(FANOUT_EXCHANGE);
    }

    /*
     * Registrujemo bean koji ce sluziti za konekciju na RabbitMQ gde se mi u
     * primeru kacimo u lokalu.
     */
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory("localhost");
        return connectionFactory;
    }
    @Bean
    Binding bindingAgency1(Queue queue, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(queue).to(fanoutExchange);
    }

    // Binding za drugi Queue
    @Bean
    Binding bindingAgency2(Queue queue2, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(queue2).to(fanoutExchange);
    }

    // Binding za treći Queue
    @Bean
    Binding bindingAgency3(Queue queue3, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(queue3).to(fanoutExchange);
    }
}
