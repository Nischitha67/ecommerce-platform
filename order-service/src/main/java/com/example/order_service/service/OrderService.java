package com.example.order_service.service;

import com.example.cart_service.event.CartCheckedOutEvent;
import com.example.order_service.model.Order;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final List<Order> orders = new ArrayList<>();

    public void createOrder(CartCheckedOutEvent event) {
        Order order = new Order();
        order.setUserId(event.userId());
        order.setItems(event.items());
        order.setStatus("CREATED");
        orders.add(order);
        System.out.println("Order created for user: " + event.userId());
    }

    public List<Order> getOrders() { return orders; }
}
