package com.shopping.domain.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "Authentication Response DTO",description = "Authentication Response Data Transfer Object")
public class AuthToken {
    private String token;
    private String username;
}
