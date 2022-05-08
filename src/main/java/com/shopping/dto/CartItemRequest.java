package com.shopping.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@Builder
public class CartItemRequest {

    @Size(min = 5, message = "Username must have at least 5 characters")
    @NotEmpty(message = "Username must not be empty")
    private String username;

    @Size(min = 3, message = "Serial number have at least 3 characters")
    @NotEmpty(message = "Serial number must not be empty")
    private String serialNumber;

    @Min(value = 0, message = "Quantity must not to be negative number")
    private int quantity;
}
