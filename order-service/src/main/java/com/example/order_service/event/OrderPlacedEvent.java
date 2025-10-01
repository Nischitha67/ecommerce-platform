package com.example.order_service.event;

import java.util.List;
import com.example.order_service.model.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderPlacedEvent {
    private String orderId;
    private String userId;
    private List<Order> orders;
}
