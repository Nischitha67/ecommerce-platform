package com.example.order_service.model;

import jakarta.persistence.*;

@Entity
@Table(name = "order_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment ID
    private Long id;

    private Long productId;

    private int quantity;

    public CartItem() {}

    public CartItem(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
