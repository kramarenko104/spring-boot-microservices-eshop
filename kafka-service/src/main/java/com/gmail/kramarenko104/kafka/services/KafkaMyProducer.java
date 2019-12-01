package com.gmail.kramarenko104.kafka.services;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
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
        Properties producerProperties = new Properties();
        /* Defining producer properties */
        producerProperties.put("bootstrap.servers", kafkaBootstrapServers);
        producerProperties.put("acks", "all");  // acknowledge from leader and from replicas
        producerProperties.put("retries", 2);   // after 2 unsuccessful retries sending becomes failed
        producerProperties.put("linger.ms", 1); // every millisecond record will be sent
        producerProperties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerProperties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        producer = new KafkaProducer<String, String>(producerProperties);
    }

    public void sendMessage(String  key, String message) {
        logger.debug(String.format("[eshop] #### -> Producing message -> %s", message));
        ProducerRecord<String, String> record = new ProducerRecord<>(MY_TOPIC, key, message);
        producer.send(record);
        producer.close();
    }

}



