package com.gmail.kramarenko104.kafka.services;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

@Component
public class KafkaMyConsumer {

    private final Logger logger = LoggerFactory.getLogger(KafkaMyConsumer.class);

    @Value("${kafka.bootstrap.servers}")
    private String kafkaBootstrapServers;

    @Value("${kafka.template.default-topic}")
    private String topic;

    @Value("${kafka.consumer.group-id}")
    private String groupId;

    private Consumer<String, String> consumer;

    private List<String> kafkaMessages;

    public KafkaMyConsumer() {
        kafkaMessages = new ArrayList<>();
    }

    public void configure() {
        Properties consumerProperties = new Properties();
        // Defining consumer properties
        // establishing the initial connection to the Kafka cluster:
        consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServers);
        // group.id property is required if the consumer uses either the group management functionality by using subscribe(topic)
        // or the Kafka-based offset management strategy:
        consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        consumerProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumerProperties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        consumerProperties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 1000);
        consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        consumer = new KafkaConsumer<String, String>(consumerProperties);
    }

    public String receiveMessage() {
        consumer.subscribe(Collections.singletonList(topic));
        CountDownLatch latch = new CountDownLatch(1);
        Runnable consumerRunnable = new ConsumerRunnable(latch);
        new Thread(consumerRunnable).start();

        // add shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("[eshop] Caught shutdown hook");
            ((ConsumerRunnable) consumerRunnable).shutdown();
            try {
                latch.await();
            } catch (InterruptedException e) {
                logger.error("[eshop] App is interrupted ", e);
            }
            logger.info("[eshop] App has exited");
        }));

        try {
            latch.await();
        } catch (InterruptedException e) {
            logger.error("[eshop] App is interrupted ", e);
        }
        return kafkaMessages.stream().collect(Collectors.joining("\n"));
    }

    private class ConsumerRunnable implements Runnable {

        private final CountDownLatch latch;

        public ConsumerRunnable(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void run() {
            String message;
            try {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(10000));
                for (ConsumerRecord<String, String> record : records) {
                    message = String.format("Consumed message -> %s with key: %s, partition: %s, offset: %s",
                            record.value(), record.key(), record.partition(), record.offset());
                    logger.debug("[eshop] " + message);
                    kafkaMessages.add(message);
                }
            } catch (WakeupException ex) {
                logger.info("[eshop] KafkaConsumer received shutdown signal...");
            } finally {
                consumer.close();
                // tell KafkaConsumer that ConsumerThread is done
                latch.countDown();
            }
        }

        public void shutdown() {
            // interrupt consumer.poll()
            consumer.wakeup();
        }
    }
}
