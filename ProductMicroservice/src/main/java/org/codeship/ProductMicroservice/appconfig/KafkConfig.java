package org.codeship.ProductMicroservice.appconfig;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkConfig {

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
