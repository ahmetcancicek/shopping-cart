package com.shopping.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@Builder
@ApiModel(value = "Cart Item Request DTO", description = "Cart Item Request Data Transfer Object")
public class CartItemRequest {

    @ApiModelProperty(value = "serialNumber", required = true)
    @Size(min = 3, message = "Serial number have at least 3 characters")
    @NotEmpty(message = "Serial number must not be empty")
    private String serialNumber;

    @ApiModelProperty(value = "serialNumber", required = false)
    @Min(value = 0, message = "Quantity must not to be negative number")
    private int quantity;
}
