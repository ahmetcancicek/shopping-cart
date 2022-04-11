package com.shopping.service;

import com.shopping.model.Customer;
import com.shopping.model.Order;
import com.shopping.model.Payment;

public interface OrderService {

    Order save(Order order);

    Order createOrder(Customer customer);

    Order cancel(Order order);

    Order cancel(Long id);

    Order complete(Order order);

    Order complete(Long id);

    Order applyPayment(Order order, Payment payment);
}
