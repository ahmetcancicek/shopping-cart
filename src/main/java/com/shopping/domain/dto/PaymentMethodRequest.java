package com.shopping.domain.dto;

import com.shopping.domain.model.PaymentType;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Setter
@Getter
@Builder
@ApiModel(value = "Payment Method Request DTO",description = "Payment Method Request Data Transfer Object")
public class PaymentMethodRequest {
    private Long id;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    private String name;
}
