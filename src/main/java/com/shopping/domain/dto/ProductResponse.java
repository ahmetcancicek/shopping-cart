package com.shopping.domain.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProductResponse {
    private String serialNumber;
    private String name;
    private String description;
    public BigDecimal price;
    public Integer quantity;
}
