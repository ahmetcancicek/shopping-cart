package com.shopping.domain.dto;

import com.shopping.domain.model.PaymentType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;

@Setter
@Getter
@Builder
@ApiModel(value = "Payment Method Request DTO", description = "Payment Method Request Data Transfer Object")
public class PaymentMethodRequest {

    @ApiModelProperty(value = "paymentType", required = true)
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @ApiModelProperty(value = "name", required = true)
    @NotEmpty(message = "Name must not be empty")
    private String name;
}
