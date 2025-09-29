package com.example.cartservice.event;

import com.example.cartservice.model.CartItem;
import java.util.List;

public record CartCheckedOutEvent(String userId, List<CartItem> items) {}
