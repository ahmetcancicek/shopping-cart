package com.shopping.service;

import com.shopping.domain.dto.PaymentMethodRequest;
import com.shopping.domain.dto.PaymentMethodResponse;

public interface PaymentMethodService {
    PaymentMethodResponse save(String username, PaymentMethodRequest paymentMethodRequest);

    void deleteById(String username, PaymentMethodRequest paymentMethodRequest);

    PaymentMethodResponse findById(String username, Long id);
}
