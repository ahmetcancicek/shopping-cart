package com.shopping.domain.dto;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@ApiModel(value = "Product Response DTO",description = "Product Response Data Transfer Object")
public class ProductResponse {
    private String serialNumber;
    private String name;
    private String description;
    public BigDecimal price;
    public Integer quantity;
}
