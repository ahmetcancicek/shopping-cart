package com.shopping.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Set;

@Data
@Builder
public class CartResponse {
    private String username;
    private BigDecimal totalPrice;
    private Integer totalQuantity;
    private Set<CartItemResponse> items;
}
