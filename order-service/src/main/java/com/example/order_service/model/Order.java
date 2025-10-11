package com.example.order_service.model;

import jakarta.persistence.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String orderId; // Primary key

    private String userId;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id") // FK in order_items table
    private List<CartItem> items;

    private String status;
}
