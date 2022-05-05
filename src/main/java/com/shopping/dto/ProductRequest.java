package com.shopping.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProductRequest {
    private String serialNumber;
    private String name;
    private String description;
    public BigDecimal price;
    public Integer quantity;
}
