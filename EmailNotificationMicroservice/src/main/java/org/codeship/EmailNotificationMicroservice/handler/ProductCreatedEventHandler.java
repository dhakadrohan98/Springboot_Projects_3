package org.codeship.EmailNotificationMicroservice.handler;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ProductCreatedEventHandler {

    @KafkaListener(topics = "product-created-events-topic")
    public void handle(ProductCreatedEvent productCreatedEvent) {
        
    }
}
