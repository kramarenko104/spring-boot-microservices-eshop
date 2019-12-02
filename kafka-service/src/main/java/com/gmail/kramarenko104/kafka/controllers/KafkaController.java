package com.gmail.kramarenko104.kafka.controllers;

import com.gmail.kramarenko104.kafka.services.KafkaMyConsumer;
import com.gmail.kramarenko104.kafka.services.KafkaMyProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/kafka")
public class KafkaController {

    private static final Logger logger = LoggerFactory.getLogger(KafkaController.class);

    private final KafkaMyProducer producer;

    private final KafkaMyConsumer consumer;

    @Autowired
    public KafkaController(KafkaMyProducer producer, KafkaMyConsumer consumer) {
        this.producer = producer;
        this.consumer = consumer;
    }

    @PostMapping("/send")
    public HttpEntity<String> sendMessageToKafkaTopic(@RequestParam String key,
                                                      @RequestBody String message) {
        producer.configure();
        producer.sendMessage(key,  message);
        logger.debug("[eshop] Message sent to Kafka: " + message);
        return new ResponseEntity<String>(message, HttpStatus.OK);
    }

    @GetMapping("/receive")
    public HttpEntity<String> receiveMessageFromKafkaTopic(@RequestParam String topic) {
        consumer.configure();
        String messageFromKafka = consumer.receiveMessage();
        logger.debug("[eshop] Message got from Kafka: " + messageFromKafka);
        return new ResponseEntity<String>(messageFromKafka, HttpStatus.OK);
    }
}
