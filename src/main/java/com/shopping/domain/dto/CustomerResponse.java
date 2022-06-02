package com.shopping.domain.dto;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@ApiModel(value = "Customer Response DTO",description = "Customer Response Data Transfer Object")
public class CustomerResponse {
    private String firstName;
    private String lastName;
    private String email;
    private String username;
}
