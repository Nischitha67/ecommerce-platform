package com.example.cart_service.service;

import com.example.cart_service.model.CartItem;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    private final List<CartItem> cartItems = new ArrayList<>();

    public void addItem(CartItem item) { cartItems.add(item); }
    public List<CartItem> getCart() { return cartItems; }
}
