package com.example.order_service.service;

import com.example.order_service.event.CartCheckedOutEvent;
import com.example.order_service.model.Order;
import com.example.order_service.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public void createOrder(CartCheckedOutEvent event) {
        Order order = new Order();
        order.setUserId(event.getUserId());
        order.setItems(event.getItems());
        order.setStatus("CREATED");
        orderRepository.save(order);
    }

    public List<Order> getOrders() {
        return orderRepository.findAll();
    }
}
