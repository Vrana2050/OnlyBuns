package com.example.messagebroker.model;


import java.time.LocalDateTime;
import java.util.UUID;

public class Message {

    private String id;
    private String body;
    private LocalDateTime receivedTimestamp;
    private LocalDateTime deliveryTimestamp;
    private boolean acknowledged;
    private int deliveryAttempts;

    public Message(String body) {
        this.id = UUID.randomUUID().toString();
        this.body = body;
        this.receivedTimestamp = LocalDateTime.now();
        this.deliveryTimestamp = null;
        this.acknowledged = false;
        this.deliveryAttempts = 0;
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

    public LocalDateTime getReceivedTimestamp() {
        return receivedTimestamp;
    }

    public LocalDateTime getDeliveryTimestamp() {
        return deliveryTimestamp;
    }

    public void setDeliveryTimestamp(LocalDateTime deliveryTimestamp) {
        this.deliveryTimestamp = deliveryTimestamp;
    }

    public boolean isAcknowledged() {
        return acknowledged;
    }

    public void acknowledge() {
        this.acknowledged = true;
    }

    public int getDeliveryAttempts() {
        return deliveryAttempts;
    }

    public void incrementDeliveryAttempts() {
        this.deliveryAttempts++;
    }

    public boolean isAckDeadlineExceeded(){
        return !this.isAcknowledged() &&
                this.deliveryAttempts > 0 &&
                LocalDateTime.now().isAfter(this.deliveryTimestamp.plusSeconds(10));
    }
}

