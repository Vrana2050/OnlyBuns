package com.example.messagebroker.queue;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueueConfig {

    @Bean
    public QueueManager queueManager() {
        return new QueueManager();
    }
}
