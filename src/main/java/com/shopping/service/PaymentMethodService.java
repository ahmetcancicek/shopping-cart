package com.shopping.service;

import com.shopping.domain.dto.PaymentMethodRequest;
import com.shopping.domain.dto.PaymentMethodResponse;

public interface PaymentMethodService {
    PaymentMethodResponse save(String username, PaymentMethodRequest paymentMethodRequest);

    void deleteById(String username,  Long id);

    PaymentMethodResponse findById(String username, Long id);
}
