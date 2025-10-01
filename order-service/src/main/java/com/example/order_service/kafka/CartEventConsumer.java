package com.example.order_service.kafka;

import com.example.order_service.event.CartCheckedOutEvent;
import com.example.order_service.service.OrderService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class CartEventConsumer {

    private final OrderService orderService;

    public CartEventConsumer(OrderService orderService) {
        this.orderService = orderService;
    }

    @KafkaListener(topics = "cart-checkedout-topic", groupId = "order-group")
    public void handleCartCheckedOut(CartCheckedOutEvent event) {
        System.out.println("Received CartCheckedOutEvent: " + event);
        orderService.createOrder(event);
    }
}
