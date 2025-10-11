package com.example.cart_service.event;

import com.example.cart_service.model.CartItem;
import java.util.List;

public record CartCheckedOutEvent(String userId, List<CartItem> items) {}
