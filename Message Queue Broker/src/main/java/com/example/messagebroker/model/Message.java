package com.example.messagebroker.model;


import java.time.LocalDateTime;
import java.util.UUID;

public class Message {

    private String id;
    private String body;
    private LocalDateTime timestamp;
    private boolean acknowledged;

    public Message(String body) {
        this.id = UUID.randomUUID().toString();
        this.body = body;
        this.timestamp = LocalDateTime.now();
        this.acknowledged = false;
    }

    public String getId() {
        return id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public boolean isAcknowledged() {
        return acknowledged;
    }

    public void acknowledge() {
        this.acknowledged = true;
    }
}

