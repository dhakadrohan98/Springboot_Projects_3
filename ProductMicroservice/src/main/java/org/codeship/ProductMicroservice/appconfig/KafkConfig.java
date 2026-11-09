package org.codeship.ProductMicroservice.appconfig;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.codeship.ProductMicroservice.event.ProductCreatedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkConfig {

    @Value("${spring.kafka.producer.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.producer.key-serializer}")
    private String keySerializer;

    @Value("${spring.kafka.producer.value-serializer}")
    private String valueSerializer;

    @Value("${spring.kafka.producer.acks}")
    private String acks;

    @Value("${spring.kafka.producer.properties.delivery.timeout.ms}")
    private String deliveryTimeout;

    @Value("${spring.kafka.producer.properties.linger.ms}")
    private String linger;

    @Value("${spring.kafka.producer.properties.request.timeout.ms}")
    private String requestTimeout;

    @Value("${spring.kafka.producer.properties.enable.idempotence}")
    private boolean idempotence;

    @Value("${spring.kafka.producer.properties.max.in.flight.requests.per.connection}")
    private String maxInFlightRequests;

    public Map<String, Object> createProducerConfigs() {
        Map<String, Object> configMap = new HashMap<>();
        configMap.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configMap.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
        configMap.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);
        configMap.put(ProducerConfig.ACKS_CONFIG, acks);
        configMap.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, deliveryTimeout);
        configMap.put(ProducerConfig.LINGER_MS_CONFIG, linger);
        configMap.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, requestTimeout);
        configMap.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, idempotence);
        configMap.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, maxInFlightRequests);
//        Default value assigned to RETRIES_CONFIG property
//        configMap.put(ProducerConfig.RETRIES_CONFIG, Integer.MAX_VALUE);
        return configMap;
    }

    @Bean
    public ProducerFactory<String, ProductCreatedEvent> produerFactory() {
        return new DefaultKafkaProducerFactory<>(createProducerConfigs());
    }

    @Bean
    public KafkaTemplate<String, ProductCreatedEvent> createKafkaTemplate() {
        return new KafkaTemplate<>(produerFactory());
    }

    @Bean
    public NewTopic createTopic() {
        Map<String, String> hmap = new HashMap<>();
        hmap.put("min.insync.replicas", "2");
        return TopicBuilder
                .name("product-created-events-topic")
                .partitions(3)
                .replicas(3)
                .configs(hmap)
                .build();
    }
}
