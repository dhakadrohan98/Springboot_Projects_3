package org.codeship.ProductMicroservice.service.impl;

import org.codeship.ProductMicroservice.dto.CreateProductRestModel;
import org.codeship.ProductMicroservice.event.ProductCreatedEvent;
import org.codeship.ProductMicroservice.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class ProductServiceImpl implements ProductService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;

    public ProductServiceImpl(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public String createProduct(CreateProductRestModel createProductRestModel) {

        String productId = UUID.randomUUID().toString();
        //TODO: persist the product data into database
        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent(productId, createProductRestModel.getTitle(),
                createProductRestModel.getPrice(), createProductRestModel.getQuantity());
        logger.info("****publishing message to kafka topic");

        //publish the event data as topic-name, key, value {key:value} pair
        CompletableFuture<SendResult<String, ProductCreatedEvent>> future =
                kafkaTemplate.send("product-created-events-topic", productId, productCreatedEvent);
        //for asynchronous call, if publish event fails then also it will receive the result not the excpetion
        future.whenComplete((result, exception) -> {
            if(exception != null) {
                logger.error("*****Failed to send message: " + exception.getMessage());
            } else {
                logger.info("*****Message sent successfully: " + result.getRecordMetadata());
            }
        });
        // use join() method to block the current thread (to stop any other task which are later after future.whenComplete(result, exception) method call)
        //future.join();
        logger.info("*****returning product id");
        return productId;
    }
}
