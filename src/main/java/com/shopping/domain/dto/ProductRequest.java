package com.shopping.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Product Request DTO", description = "Product Request Data Transfer Object")
public class ProductRequest {

    @ApiModelProperty(value = "serialNumber", required = true)
    @NotEmpty(message = "Serial number must not be empty")
    @Size(min = 3, message = "Serial number have at least 3 characters")
    private String serialNumber;

    @ApiModelProperty(value = "name", required = true)
    @Size(min = 3, message = "Name must have at least 5 characters")
    @NotEmpty(message = "Name must not be empty")
    private String name;

    @ApiModelProperty(value = "description", required = false)
    private String description;

    @ApiModelProperty(value = "price", required = true)
    @Column(name = "price", nullable = false)
    @DecimalMin(value = "0.00", message = "Price must not to be negative number")
    public BigDecimal price;

    @ApiModelProperty(value = "quantity", required = true)
    @Min(value = 0, message = "Quantity must not to be negative number")
    public Integer quantity;
}
