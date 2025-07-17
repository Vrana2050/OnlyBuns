package com.example.messagebroker.queue;

import com.example.messagebroker.model.Message;

import java.util.HashMap;
import java.util.Map;

public class QueueManager {
    private final Map<String, MessageQueue> queues = new HashMap<>();
    private final Map<String, Exchange> exchanges = new HashMap<>();

    public MessageQueue createQueue(String name) {
        return queues.computeIfAbsent(name, MessageQueue::new);
    }

    public Exchange createExchange(String name, Exchange.ExchangeType type) {
        return exchanges.computeIfAbsent(name, key -> new Exchange(name, type));
    }

    public void bindQueue(String queueName, String exchangeName, String routingKey) {
        MessageQueue q = queues.get(queueName);
        Exchange e = exchanges.get(exchangeName);
        if (q != null && e != null) {
            e.bindQueue(routingKey, q);
        }
    }

    public void publish(String exchangeName, String routingKey, String body) {
        Exchange e = exchanges.get(exchangeName);
        if (e != null) {
            Message msg = new Message(body);
            String newBody = String.format("{\"id\":\"%s\", %s}", msg.getId(), body);
            newBody = newBody.replace(", {\"name", ", \"name");
            newBody = newBody.substring(0, newBody.length() - 1);
            msg.setBody(newBody);
            e.publishMessage(routingKey, msg);
        }
    }

    public Message consume(String queueName) {
        MessageQueue q = queues.get(queueName);
        return q != null ? q.pollMessage() : null;
    }

    public void ack(String queueName, String messageId) {
        MessageQueue q = queues.get(queueName);
        if (q != null) {
            q.ack(messageId);
        }
    }

    public void requeue(String queueName, String messageId) {
        MessageQueue q = queues.get(queueName);
        if (q != null) {
            q.requeue(messageId);
        }
    }
}