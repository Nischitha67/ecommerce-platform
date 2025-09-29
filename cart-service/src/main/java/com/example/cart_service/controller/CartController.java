package com.example.cartservice.controller;

import com.example.cartservice.event.CartCheckedOutEvent;
import com.example.cartservice.model.CartItem;
import com.example.cartservice.kafka.CartEventProducer;
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
