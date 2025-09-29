package com.example.cartservice.kafka;

import com.example.cartservice.event.CartCheckedOutEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CartEventProducer {

    private final KafkaTemplate<String, CartCheckedOutEvent> kafkaTemplate;

    public CartEventProducer(KafkaTemplate<String, CartCheckedOutEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishCartCheckedOut(CartCheckedOutEvent event) {
        kafkaTemplate.send("cart-checkedout-topic", event);
        System.out.println("Published CartCheckedOutEvent: " + event);
    }
}
