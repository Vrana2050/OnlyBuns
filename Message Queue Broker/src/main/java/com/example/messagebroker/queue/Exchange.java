package com.example.messagebroker.queue;

import com.example.messagebroker.model.Message;

import java.util.*;


public class Exchange {

    public enum ExchangeType {
        DIRECT,
        FANOUT
    }

    private final String name;
    private final ExchangeType type;
    private final Map<String, List<MessageQueue>> bindings = new HashMap<>();

    public Exchange(String name, ExchangeType type) {
        this.name = name;
        this.type = type;
    }

    public void bindQueue(String routingKey, MessageQueue queue) {
        bindings.computeIfAbsent(routingKey.trim(), k -> new ArrayList<>()).add(queue);
    }

    public void publishMessage(String routingKey, Message msg) {
        if (type == ExchangeType.DIRECT) {
            List<MessageQueue> queues = bindings.getOrDefault(routingKey.trim(), List.of());
            for (MessageQueue q : queues) {
                q.addMessage(msg);
            }
        } else if (type == ExchangeType.FANOUT) {
            Set<MessageQueue> allQueues = new HashSet<>();
            for (List<MessageQueue> qList : bindings.values()) {
                allQueues.addAll(qList);
            }
            for (MessageQueue q : allQueues) {
                q.addMessage(msg);
            }
        }
    }
}

