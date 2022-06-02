package com.shopping.service.impl;

import com.shopping.domain.dto.PaymentMethodRequest;
import com.shopping.domain.dto.PaymentMethodResponse;;
import com.shopping.domain.exception.NoSuchElementFoundException;
import com.shopping.domain.mapper.PaymentMethodMapper;
import com.shopping.domain.model.Customer;
import com.shopping.domain.model.PaymentMethod;
import com.shopping.repository.PaymentMethodRepository;
import com.shopping.service.CustomerService;
import com.shopping.service.PaymentMethodService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@AllArgsConstructor
@Service
public class PaymentMethodServiceImpl implements PaymentMethodService {

    private final PaymentMethodRepository paymentMethodRepository;
    private final CustomerService customerService;

    @Transactional
    @Override
    public PaymentMethodResponse save(String username, PaymentMethodRequest paymentMethodRequest) {
        Customer customer = customerService.findCustomerByUsername(username);

        PaymentMethod paymentMethod = PaymentMethodMapper.INSTANCE.toPaymentMethod(paymentMethodRequest);
        paymentMethod.setCustomer(customer);

        PaymentMethod savedPaymentMethod = paymentMethodRepository.save(paymentMethod);
        log.info("new payment method has been created with id: {}", savedPaymentMethod.getId());
        return PaymentMethodMapper.INSTANCE.fromPaymentMethod(savedPaymentMethod);
    }

    @Transactional
    @Override
    public void deleteById(String username, Long id) {
        findById(username, id);
        paymentMethodRepository.deleteById(id);
        log.info("payment method has been deleted with id: {}", id);
    }

    @Override
    public PaymentMethodResponse findById(String username, Long id) {
        return PaymentMethodMapper.INSTANCE.fromPaymentMethod(
                paymentMethodRepository.findByIdAndCustomer_User_Username(id, username).orElseThrow(() -> {
                    log.error("payment method does not exist with id: {}", id);
                    return new NoSuchElementFoundException(String.format("payment does not exist with id: {%d}", id));
                }));
    }
}
