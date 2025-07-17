package com.example.messagebroker.queue;

import com.example.messagebroker.model.Message;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;


public class MessageQueue {

    private final String name;
    private final Queue<Message> messages = new LinkedList<>();
    private final Map<String, Message> inFlight = new HashMap<>();

    public MessageQueue(String name) {
        this.name = name;
    }

    public synchronized void addMessage(Message msg) {
        messages.add(msg);
    }

    public synchronized Message pollMessage() {
        Message msg = messages.poll();
        if (msg != null) {
            inFlight.put(msg.getId(), msg);
        }
        return msg;
    }

    public synchronized void ack(String messageId) {
        inFlight.remove(messageId);
    }

    public synchronized void requeue(String messageId) {
        Message msg = inFlight.remove(messageId);
        if (msg != null) {
            messages.add(msg);
        }
    }

    public String getName() {
        return name;
    }
}