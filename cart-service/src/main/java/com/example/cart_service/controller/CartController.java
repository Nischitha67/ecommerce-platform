package com.example.cart_service.controller;

import com.example.cart_service.event.CartCheckedOutEvent;
import com.example.cart_service.model.CartItem;
import com.example.cart_service.kafka.CartEventProducer;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartEventProducer producer;

    public CartController(CartEventProducer producer) {
        this.producer = producer;
    }

    @PostMapping("/checkout")
    public String checkout(@RequestHeader("userId") String userId, @RequestBody List<CartItem> items) {
        CartCheckedOutEvent event = new CartCheckedOutEvent(userId, items);
        producer.publishCartCheckedOut(event);
        return "Cart checked out and event published!";
    }
}
