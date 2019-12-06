package com.gmail.kramarenko104.kafka.controllers;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/kafka")
public class KafkaController {

    private static final Logger logger = LoggerFactory.getLogger(KafkaController.class);

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Value("${spring.kafka.topic}")
    private String topicName;

    private List<String> kafkaMessages;

    public KafkaController() {
        kafkaMessages = new ArrayList<>();
    }

    @PostMapping("/send")
    public HttpEntity<String> sendMessageToKafkaTopic(@PathParam("key") String key,
                                                      @RequestBody String message) {
        logger.debug("[eshop] #### Sending Message -> topic:'{}', key:{}, message:{}", topicName, key, message);
        kafkaTemplate.send(topicName, key, message);
        return new ResponseEntity<String>(message, HttpStatus.OK);
    }

    @GetMapping("/receive")
    // run command before GET request 'http://localhost:9000/kafka/receive' to see topic from the beginning (stop kafka-service before this):
    // kafka-consumer-groups --bootstrap-server localhost:9092 --group test --reset-offsets --to-earliest --execute -topic julia_topic
    public HttpEntity<String> getKafkaMessages() {
        String result = kafkaMessages.stream().collect(Collectors.joining("\n"));
        logger.debug("[eshop] #### Consumed Message -> " + result);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @KafkaListener(topics = "${spring.kafka.topic}", groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory")
    public void listenKafkaTopic(ConsumerRecord<String, String> record) {
        String message = String.format("key: %s, partition: %s, offset: %s for message: %s",
                record.key(), record.partition(), record.offset(), record.value());
        kafkaMessages.add(message);
    }
}
