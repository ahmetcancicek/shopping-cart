package com.shopping.service;

import com.shopping.domain.dto.PaymentMethodRequest;
import com.shopping.domain.dto.PaymentMethodResponse;

public interface PaymentMethodService {
    PaymentMethodResponse save(String username, PaymentMethodRequest paymentMethodRequest);
    void delete(String username, PaymentMethodResponse paymentMethodResponse);
}
