package com.example.messagebroker.controller;

import com.example.messagebroker.model.Message;
import com.example.messagebroker.queue.Exchange;
import com.example.messagebroker.queue.QueueManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://127.0.0.1:5501/")
@RestController
@RequestMapping("/broker")
public class MessageBrokerController {

    private final QueueManager queueManager;

    public MessageBrokerController(QueueManager queueManager) {
        this.queueManager = queueManager;
    }

    @PostMapping("/queue/{name}")
    public ResponseEntity<String> createQueue(@PathVariable String name) {
        queueManager.createQueue(name);
        return ResponseEntity.ok("Queue created: " + name);
    }

    @PostMapping("/exchange/{name}")
    public ResponseEntity<String> createExchange(@PathVariable String name,
                                                 @RequestParam Exchange.ExchangeType type) {
        queueManager.createExchange(name, type);
        return ResponseEntity.ok("Exchange created: " + name + " of type " + type);
    }

    @PostMapping("/bind")
    public ResponseEntity<String> bindQueue(@RequestParam String queue,
                                            @RequestParam String exchange,
                                            @RequestParam String routingKey) {
        queueManager.bindQueue(queue, exchange, routingKey);
        return ResponseEntity.ok("Queue " + queue + " bound to exchange " + exchange + " with key " + routingKey);
    }

    @PostMapping("/publish")
    public ResponseEntity<String> publishMessage(@RequestParam String exchange,
                                                 @RequestParam String routingKey,
                                                 @RequestBody String body) {
        queueManager.publish(exchange, routingKey, body);
        return ResponseEntity.ok("Message published to exchange " + exchange);
    }

    @GetMapping("/consume/{queue}")
    public ResponseEntity<String> consumeMessage(@PathVariable String queue) {
        Message message = queueManager.consume(queue);
        if (message == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(message.getBody());
    }

    @PostMapping("/ack")
    public ResponseEntity<String> ackMessage(@RequestParam String queue,
                                             @RequestParam String messageId) {
        queueManager.ack(queue, messageId);
        return ResponseEntity.ok("Message " + messageId + " acknowledged in queue " + queue);
    }

    @PostMapping("/requeue")
    public ResponseEntity<String> requeueMessage(@RequestParam String queue,
                                                 @RequestParam String messageId) {
        queueManager.requeue(queue, messageId);
        return ResponseEntity.ok("Message " + messageId + " requeued in queue " + queue);
    }
}
