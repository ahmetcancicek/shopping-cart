package com.shopping.domain.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CartItemResponse {
    private String serialNumber;
    private String name;
    private String description;
    private Integer quantity;
    private BigDecimal price;
}
