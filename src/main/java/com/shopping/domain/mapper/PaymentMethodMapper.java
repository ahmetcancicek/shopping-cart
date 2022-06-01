package com.shopping.domain.mapper;

import com.shopping.domain.dto.PaymentMethodRequest;
import com.shopping.domain.dto.PaymentMethodResponse;
import com.shopping.domain.model.PaymentMethod;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PaymentMethodMapper {
    PaymentMethodMapper INSTANCE = Mappers.getMapper(PaymentMethodMapper.class);

    PaymentMethod toPaymentMethod(PaymentMethodRequest paymentMethodRequest);

    PaymentMethodResponse fromPaymentMethod(PaymentMethod paymentMethod);
}
