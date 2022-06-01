package com.shopping.domain.dto;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@Builder
public class ProductRequest {

    @NotEmpty(message = "Serial number must not be empty")
    @Size(min = 3, message = "Serial number have at least 3 characters")
    private String serialNumber;

    @Size(min = 3, message = "Name must have at least 5 characters")
    @NotEmpty(message = "Name must not be empty")
    private String name;

    private String description;

    @Column(name = "price", nullable = false)
    @DecimalMin(value = "0.00", message = "Price must not to be negative number")
    public BigDecimal price;

    @Min(value = 0, message = "Quantity must not to be negative number")
    public Integer quantity;
}
