package com.shopping.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "API Response DTO", description = "API Response Data Transfer Object")
public class ApiResponse<T> {

    @ApiModelProperty(value = "status")
    private int status;

    @ApiModelProperty(value = "message")
    private String message;

    @ApiModelProperty(value = "data")
    private T data;
}