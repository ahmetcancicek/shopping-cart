package com.shopping.unit.service.impl;

import com.shopping.domain.model.*;
import com.shopping.repository.OrderRepository;
import com.shopping.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
public class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void it_should_save_order() {
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

        Address address = Address.builder()
                .id(1L)
                .street("43 Grand Street")
                .city("New York")
                .zipCode("00501")
                .stateCode("+1")
                .customer(customer)
                .build();

        Order order = Order.builder()
                .customer(customer)
                .billingAddress(address)
                .shippingAddress(address)
                .orderKey("HA702790B")
                .orderStatus(OrderStatus.NEW)
                .build();

        given(orderRepository.save(any())).willReturn(order);

        // when


        // then
    }

    @Test
    void it_should_create_order() {
        // given

        // when

        // then
    }

    @Test
    void it_should_cancel_order_with_order() {
        // given

        // when

        // then
    }

    @Test
    void it_should_cancel_order_with_id() {
        // given

        // when

        // then
    }

    @Test
    void it_should_complete_order_with_order() {
        // given

        // when

        // then
    }

    @Test
    void it_should_complete_order_with_id() {
        // given

        // when

        // then
    }

    @Test
    void it_should_apply_payment_to_order() {
        // given

        // when

        // then
    }
}
