package com.shopping.service.impl;

import com.shopping.domain.dto.PaymentMethodRequest;
import com.shopping.domain.dto.PaymentMethodResponse;
import com.shopping.domain.model.Customer;
import com.shopping.domain.model.PaymentMethod;
import com.shopping.domain.model.PaymentType;
import com.shopping.domain.model.User;
import com.shopping.repository.PaymentMethodRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PaymentMethodServiceImplTest {

    @Mock
    private PaymentMethodRepository paymentMethodRepository;

    @InjectMocks
    private PaymentMethodServiceImpl paymentMethodService;

    @Mock
    private CustomerServiceImpl customerService;

    @Test
    public void it_should_add_payment_method() {
        // given
        Customer customer = Customer.builder()
                .id(1L)
                .firstName("Lucy")
                .lastName("King")
                .user(User.builder()
                        .id(1L)
                        .active(true)
                        .username("lucyking")
                        .email("lucyking@email.com")
                        .password("LA4SD12")
                        .build())
                .build();

        PaymentMethod paymentMethod = PaymentMethod.builder()
                .id(1L)
                .paymentType(PaymentType.VISA)
                .name("My VISA")
                .customer(customer)
                .build();

        PaymentMethodRequest paymentMethodRequest = PaymentMethodRequest.builder()
                .id(1L)
                .paymentType(PaymentType.VISA)
                .name("My VISA")
                .build();

        given(customerService.findCustomerByUsername(any())).willReturn(customer);
        given(paymentMethodRepository.save(any())).willReturn(paymentMethod);

        // when
        PaymentMethodResponse expectedPaymentMethodResponse = paymentMethodService.save(
                customer.getUser().getUsername(),
                paymentMethodRequest);

        // then
        verify(paymentMethodRepository, times(1)).save(any());
        assertEquals(paymentMethod.getId(), expectedPaymentMethodResponse.getId(), "Id must be equal");
        assertEquals(paymentMethod.getName(), expectedPaymentMethodResponse.getName(), "Name must be equal");
        assertEquals(paymentMethod.getPaymentType().name(), expectedPaymentMethodResponse.getPaymentType().name(), "Name must be equal");
    }

    @Test
    public void it_should_delete_payment_method() {
        // given
        Customer customer = Customer.builder()
                .id(1L)
                .firstName("Lucy")
                .lastName("King")
                .user(User.builder()
                        .id(1L)
                        .active(true)
                        .username("lucyking")
                        .email("lucyking@email.com")
                        .password("LA4SD12")
                        .build())
                .build();

        PaymentMethod paymentMethod = PaymentMethod.builder()
                .id(1L)
                .paymentType(PaymentType.VISA)
                .name("My VISA")
                .customer(customer)
                .build();


        given(paymentMethodRepository.findByIdAndCustomer_User_Username(any(), any())).willReturn(Optional.of(paymentMethod));

        // when
        paymentMethodService.deleteById(customer.getUser().getUsername(),paymentMethod.getId());

        // then
        verify(paymentMethodRepository, times(1)).deleteById(any());
    }

    @Test
    public void it_should_return_payment_method_of_that_username_of_customer() {
        // given
        Customer customer = Customer.builder()
                .id(1L)
                .firstName("Lucy")
                .lastName("King")
                .user(User.builder()
                        .id(1L)
                        .active(true)
                        .username("lucyking")
                        .email("lucyking@email.com")
                        .password("LA4SD12")
                        .build())
                .build();

        PaymentMethod paymentMethod = PaymentMethod.builder()
                .id(1L)
                .paymentType(PaymentType.VISA)
                .name("My VISA")
                .customer(customer)
                .build();

        given(paymentMethodRepository.findByIdAndCustomer_User_Username(any(), any())).willReturn(Optional.of(paymentMethod));

        // when
        PaymentMethodResponse expectedPaymentMethodResponse = paymentMethodService.findById(customer.getUser().getUsername(), paymentMethod.getId());

        // then
        verify(paymentMethodRepository, times(1)).findByIdAndCustomer_User_Username(any(), any());
        assertEquals(paymentMethod.getId(), expectedPaymentMethodResponse.getId(), "Id must be equal");
        assertEquals(paymentMethod.getName(), expectedPaymentMethodResponse.getName(), "Name must be equal");
        assertEquals(paymentMethod.getPaymentType().name(), expectedPaymentMethodResponse.getPaymentType().name(), "Name must be equal");
    }
}
