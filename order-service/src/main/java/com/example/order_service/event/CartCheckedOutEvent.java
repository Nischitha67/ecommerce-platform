package com.example.order_service.event;

import java.util.List;
import com.example.order_service.model.CartItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartCheckedOutEvent {
    private String cartId;
    private String userId;
    private List<CartItem> items;
}
