package com.gmail.kramarenko104.kafka.services;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Properties;

@Component
public class KafkaMyProducer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaMyProducer.class);

    @Value("${kafka.template.default-topic}")
    private String MY_TOPIC;

    @Value("${kafka.bootstrap.servers}")
    private String kafkaBootstrapServers;

    private Producer<String, String> producer;

    public void configure(){
        // Defining producer properties
        Properties producerProperties = new Properties();
        producerProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServers);
        producerProperties.put(ProducerConfig.ACKS_CONFIG, "all");  // acknowledge from leader and from replicas
        producerProperties.put(ProducerConfig.RETRIES_CONFIG, 2);   // after 2 unsuccessful retries sending becomes failed
        producerProperties.put(ProducerConfig.LINGER_MS_CONFIG, 1); // every millisecond record will be sent
        producerProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producerProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        producer = new KafkaProducer<String, String>(producerProperties);
    }

    public void sendMessage(String  key, String message) {
        logger.debug(String.format("[eshop] #### -> Producing message -> %s", message));
        ProducerRecord<String, String> record = new ProducerRecord<>(MY_TOPIC, key, message);

        producer.send(record, (recordMetadata, e) -> {
            if (e == null) {
                logger.info("[eshop] #### -> Received new metadata from Kafka: \n"+
                        "Topic: " + recordMetadata.topic() +
                        ", Partition: " + recordMetadata.partition() +
                        ", Offset: " + recordMetadata.offset() +
                        ", Timestamp: " + recordMetadata.timestamp());
            } else {
                logger.error("[eshop] #### -> Error while producing message: " + e.getMessage());
            }
        });
        // flush and close producer
        producer.close();
    }

}



