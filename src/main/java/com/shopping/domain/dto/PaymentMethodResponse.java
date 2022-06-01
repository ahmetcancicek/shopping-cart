package com.shopping.domain.dto;

import com.shopping.domain.model.PaymentType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Builder
@EqualsAndHashCode(of = "id")
public class PaymentMethodResponse {
    private Long id;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    private String name;
}
