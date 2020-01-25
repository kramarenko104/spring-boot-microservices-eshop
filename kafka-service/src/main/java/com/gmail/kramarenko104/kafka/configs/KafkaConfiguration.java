package com.gmail.kramarenko104.kafka.configs;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfiguration {

    private String kafkaBootstrapServers;

    private String groupId;

    @Autowired
    public KafkaConfiguration(@Value ("${spring.kafka.bootstrap-servers}") String kafkaBootstrapServers,
                           @Value ("${spring.kafka.consumer.group-id}") String groupId) {
        this.kafkaBootstrapServers = kafkaBootstrapServers;
        this.groupId = groupId;
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServers);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        // create safe producer
        config.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true"); // prevent duplicates
        config.put(ProducerConfig.ACKS_CONFIG, "all");  // acknowledge from leader and from replicas
        config.put(ProducerConfig.RETRIES_CONFIG, Integer.toString(Integer.MAX_VALUE)) ;   // after RETRIES unsuccessful retries sending becomes failed
        // increase producer speed
        config.put(ProducerConfig.LINGER_MS_CONFIG, "10"); // every 10 millisecond batch of records will be sent
        config.put(ProducerConfig.BATCH_SIZE_CONFIG, Integer.toString(32*1024)); // batch should be = 32 Kb
        config.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
        return new DefaultKafkaProducerFactory<>(config);
    }

        @Bean
        public ConsumerFactory<String, String> consumerFactory() {
            Map<String, Object> config = new HashMap<>();
            config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServers);
            config.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
            config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
            config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
            config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
            config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
            return new DefaultKafkaConsumerFactory<>(config);
        }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

}
