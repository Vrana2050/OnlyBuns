package com.example.messagebroker.service;

import com.example.messagebroker.queue.QueueManager;
import org.springframework.stereotype.Service;

@Service
public class QueueManagerService {
    private final QueueManager manager = new QueueManager();

    public QueueManager getManager() {
        return manager;
    }
}
