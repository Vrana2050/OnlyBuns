package com.example.messagebroker.controller;

import com.example.messagebroker.model.Message;
import com.example.messagebroker.queue.Exchange;
import com.example.messagebroker.queue.QueueManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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
        if(queueManager.bindQueue(queue, exchange, routingKey))
            return ResponseEntity.ok("Queue " + queue + " bound to exchange " + exchange + " with key " + routingKey);
        return ResponseEntity.badRequest().body("Queue " + queue + " not bound to exchange " + exchange);
    }

    @PostMapping("/publish")
    public ResponseEntity<String> publishMessage(@RequestParam String exchange,
                                                 @RequestParam String routingKey,
                                                 @RequestBody String body) {
        if(queueManager.publish(exchange, routingKey, body))
            return ResponseEntity.ok("Message published to exchange " + exchange);
        return ResponseEntity.badRequest().body("Error while publishing message.");
    }

    @GetMapping("/consume/{queue}")
    public ResponseEntity<String> consumeMessage(@PathVariable String queue) {
        try {
            Optional<Message> messageOpt = queueManager.consume(queue);
            return messageOpt
                    .map(msg -> ResponseEntity.ok(msg.getBody()))
                    .orElseGet(() -> ResponseEntity.noContent().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/ack")
    public ResponseEntity<String> ackMessage(@RequestParam String queue,
                                             @RequestParam String messageId) {
        if(queueManager.ack(queue, messageId))
            return ResponseEntity.ok("Message " + messageId + " acknowledged in queue " + queue);
        return ResponseEntity.badRequest().body("Error while acknowledging message.");
    }

    @PostMapping("/requeue")
    public ResponseEntity<String> requeueMessage(@RequestParam String queue,
                                                 @RequestParam String messageId) {
        queueManager.requeue(queue, messageId);
        return ResponseEntity.ok("Message " + messageId + " required in queue " + queue);
    }
}
