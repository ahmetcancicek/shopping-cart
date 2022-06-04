package com.shopping.service;

import com.shopping.domain.dto.PaymentMethodRequest;
import com.shopping.domain.dto.PaymentMethodResponse;

public interface PaymentMethodService {
    PaymentMethodResponse save(String username, PaymentMethodRequest paymentMethodRequest);

    void deleteByIdAndUsername(Long id, String username);

    PaymentMethodResponse findByIdAndUsername(Long id, String username);
}
