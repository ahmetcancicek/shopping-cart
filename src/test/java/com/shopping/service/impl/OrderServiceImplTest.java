package com.shopping.service.impl;

import com.shopping.model.Order;
import com.shopping.repository.OrderRepository;
import com.shopping.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void it_should_save_order() {
        // given

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