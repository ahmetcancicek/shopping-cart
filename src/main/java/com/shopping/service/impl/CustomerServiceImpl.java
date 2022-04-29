package com.shopping.service.impl;

import com.shopping.exception.AlreadyExistsElementException;
import com.shopping.exception.NoSuchElementFoundException;
import com.shopping.model.Cart;
import com.shopping.model.Customer;
import com.shopping.model.User;
import com.shopping.repository.CustomerRepository;
import com.shopping.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Customer save(Customer customer) {
        customerRepository.findByUser(customer.getUser()).ifPresent((it) -> {
            log.error("user already exists");
            throw new AlreadyExistsElementException("user already exists");
        });

        if (customer.getCart() == null)
            customer.setCart(Cart.builder().customer(customer).totalPrice(BigDecimal.ZERO).build());

        Customer savedCustomer = customerRepository.save(customer);
        log.info("new customer has been created with username: {}", savedCustomer.getUser().getUsername());
        return savedCustomer;
    }

    @Override
    public Customer findById(Long id) {
        return customerRepository.findById(id).orElseThrow(() -> {
            log.error("customer does not exist with id: {}", id);
            return new NoSuchElementFoundException(String.format("customer does not exist with id: {%d}", id));
        });
    }

    @Override
    public Customer findByUser(User user) {
        return customerRepository.findByUser(user).orElseThrow(() -> {
            log.error("customer does not exist");
            return new NoSuchElementFoundException(String.format("customer does not exist"));
        });
    }

    @Override
    public void deleteById(Long id) {
        Customer customer = findById(id);
        customerRepository.deleteById(id);
        log.info("customer has been deleted: {}", customer.toString());
    }
}
