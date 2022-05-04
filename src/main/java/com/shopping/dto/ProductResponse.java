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
public class ProductResponse {
    @Length(min = 3, message = "Serial number have at least 3 characters")
    @NotEmpty(message = "Serial number must not be empty")
    private String serialNumber;

    @Length(min = 3, message = "Name must have at least 5 characters")
    @NotEmpty(message = "Name must not be empty")
    private String name;

    private String description;

    @DecimalMin(value = "0.00", message = "Price must not to be negative number")
    public BigDecimal price;

    @Min(value = 0, message = "Quantity must not to be negative number")
    public Integer quantity;
}
