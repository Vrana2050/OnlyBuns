package com.example.messagebroker.queue;

import com.example.messagebroker.model.Message;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class QueueManager {
    private final Map<String, MessageQueue> queues = new HashMap<>();
    private final Map<String, Exchange> exchanges = new HashMap<>();

    public MessageQueue createQueue(String name) {
        return queues.computeIfAbsent(name, MessageQueue::new);
    }

    public Exchange createExchange(String name, Exchange.ExchangeType type) {
        return exchanges.computeIfAbsent(name, key -> new Exchange(name, type));
    }

    public boolean bindQueue(String queueName, String exchangeName, String routingKey) {
        MessageQueue q = queues.get(queueName);
        Exchange e = exchanges.get(exchangeName);
        if (q != null && e != null) {
            e.bindQueue(routingKey, q);
            return true;
        }
        return false;
    }

    public boolean publish(String exchangeName, String routingKey, String body) {
        Exchange e = exchanges.get(exchangeName);
        if (e != null) {
            Message msg = new Message(body);
            String newBody = String.format("{\"id\":\"%s\", %s}", msg.getId(), body);
            msg.setBody(newBody.substring(0, newBody.length() - 1).replace(", {\"name", ", \"name"));

            e.publishMessage(routingKey, msg);
            return true;
        }
        return false;
    }

    public Optional<Message> consume(String queueName) {
        MessageQueue q = queues.get(queueName);
        if (q == null) {
            throw new IllegalArgumentException("Queue not found: " + queueName);
        }
        return Optional.ofNullable(q.pollMessage());
    }

    public boolean ack(String queueName, String messageId) {
        MessageQueue q = queues.get(queueName);
        if (q != null) {
            q.ack(messageId);
            return true;
        }
        return false;
    }

    public void requeue(String queueName, String messageId) {
        MessageQueue q = queues.get(queueName);
        if (q != null) {
            q.requeue(messageId);
        }
    }

    @Scheduled(fixedRate = 5000)
    public void requeueExpiredMessages() {
        for (MessageQueue queue : queues.values()) {
            queue.requeueMessages();
        }
    }


}