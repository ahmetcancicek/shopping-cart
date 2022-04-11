package com.shopping.service.impl;

import com.shopping.model.Customer;
import com.shopping.model.Order;
import com.shopping.model.Payment;
import com.shopping.service.OrderService;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {
    @Override
    public Order save(Order order) {
        return null;
    }

    @Override
    public Order createOrder(Customer customer) {
        return null;
    }

    @Override
    public Order cancel(Order order) {
        return null;
    }

    @Override
    public Order cancel(Long id) {
        return null;
    }

    @Override
    public Order complete(Order order) {
        return null;
    }

    @Override
    public Order complete(Long id) {
        return null;
    }

    @Override
    public Order applyPayment(Order order, Payment payment) {
        return null;
    }
}
