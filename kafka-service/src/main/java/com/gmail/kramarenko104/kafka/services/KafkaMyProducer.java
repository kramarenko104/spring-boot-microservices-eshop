package com.gmail.kramarenko104.kafka.services;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
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
    private String topic;

    @Value("${kafka.bootstrap.servers}")
    private String kafkaBootstrapServers;

    private Producer<String, String> producer;

    public void configure(){
        Properties producerProperties = new Properties();
        // Base producer properties
        producerProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServers);
        producerProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producerProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // create safe producer
        producerProperties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true"); // prevent duplicates
        producerProperties.put(ProducerConfig.ACKS_CONFIG, "all");  // acknowledge from leader and from replicas
        producerProperties.put(ProducerConfig.RETRIES_CONFIG, Integer.toString(Integer.MAX_VALUE)) ;   // after RETRIES unsuccessful retries sending becomes failed

        // increase producer speed
        producerProperties.put(ProducerConfig.LINGER_MS_CONFIG, "10"); // every 10 millisecond batch of records will be sent
        producerProperties.put(ProducerConfig.BATCH_SIZE_CONFIG, Integer.toString(32*1024)); // batch should be = 32 Kb
        producerProperties.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");

        producer = new KafkaProducer<String, String>(producerProperties);
    }

    public void sendMessage(String  key, String message) {
        logger.debug(String.format("[eshop] #### -> Producing message -> %s", message));
        key=null; // for testing work without key => records will be saved to different partitions
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, message);

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



