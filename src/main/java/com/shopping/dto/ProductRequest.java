package com.shopping.dto;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
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
