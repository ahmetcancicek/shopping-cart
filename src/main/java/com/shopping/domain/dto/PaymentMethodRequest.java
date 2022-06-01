package com.shopping.domain.dto;

import com.shopping.domain.model.PaymentType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Setter
@Getter
@Builder
public class PaymentMethodRequest {
    private Long id;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    private String name;
}
