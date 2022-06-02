package com.shopping.domain.dto;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Set;

@Data
@Builder
@ApiModel(value = "Cart Response DTO",description = "Cart Response Data Transfer Object")
public class CartResponse {
    private String username;
    private BigDecimal totalPrice;
    private Integer totalQuantity;
    private Set<CartItemResponse> items;
}
