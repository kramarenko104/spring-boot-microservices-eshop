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

import javax.swing.text.html.parser.Entity;

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
    public HttpStatus sendMessageToKafkaTopic(@RequestParam("message") String message) {
        producer.config();
        producer.sendMessage(message);
        return HttpStatus.OK;
    }

    @GetMapping("/receive")
    public HttpEntity<String> receiveMessageFromKafkaTopic() {
        consumer.config();
        String messageFromKafka = consumer.receiveMessage();
        logger.debug("[eshop] " + messageFromKafka);
        return new ResponseEntity<String>(messageFromKafka, HttpStatus.OK);
    }
}
