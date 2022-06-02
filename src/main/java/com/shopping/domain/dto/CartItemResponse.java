package com.shopping.domain.dto;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@ApiModel(value = "Cart Item Response DTO",description = "Cart Item Response Data Transfer Object")
public class CartItemResponse {
    private String serialNumber;
    private String name;
    private String description;
    private Integer quantity;
    private BigDecimal price;
}
