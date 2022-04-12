package com.shopping.service.impl;

import com.shopping.exception.UserAlreadyExistsException;
import com.shopping.model.Cart;
import com.shopping.model.Customer;
import com.shopping.model.User;
import com.shopping.repository.CartRepository;
import com.shopping.repository.CustomerRepository;
import com.shopping.repository.UserRepository;
import com.shopping.service.CustomerService;
import com.shopping.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository, UserRepository userRepository) {
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Customer save(Customer customer) {
        isExist(customer.getUser());

        if (customer.getCart() == null)
            customer.setCart(Cart.builder().customer(customer).build());

        Customer savedCustomer = customerRepository.save(customer);
        log.info("new customer has been created: {}", savedCustomer.getFirstName());
        return savedCustomer;
    }

    private void isExist(User user) {
        Optional<User> existing = userRepository.findByEmail(user.getEmail());
        existing.ifPresent(it -> {
            log.error("user already exists: " + it.getEmail());
            throw new UserAlreadyExistsException("user already exists: {}" + it.getEmail());
        });

        existing = userRepository.findByUsername(user.getUsername());
        existing.ifPresent(it -> {
            log.error("user already exists: " + it.getUsername());
            throw new UserAlreadyExistsException("user already exists: {}" + it.getUsername());
        });
    }


    @Override
    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    public Optional<Customer> findByUser(User user) {
        return customerRepository.findByUser(user);
    }
}
