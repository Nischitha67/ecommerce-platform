package com.example.order_service.model;

import com.example.cart_service.model.CartItem;
import java.util.List;

public class Order {
    private String userId;
    private List<CartItem> items;
    private String status;

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public List<CartItem> getItems() { return items; }
    public void setItems(List<CartItem> items) { this.items = items; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
