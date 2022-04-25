package com.shopping.service.impl;

import com.shopping.exception.UserAlreadyExistsException;
import com.shopping.exception.UserNotFoundException;
import com.shopping.model.Cart;
import com.shopping.model.Customer;
import com.shopping.model.User;
import com.shopping.repository.CustomerRepository;
import com.shopping.repository.UserRepository;
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
        isExisted(customer.getUser());

        if (customer.getCart() == null)
            customer.setCart(Cart.builder().customer(customer).totalPrice(BigDecimal.ZERO).build());

        Customer savedCustomer = customerRepository.save(customer);
        log.info("new customer has been created with username: {}", savedCustomer.getUser().getUsername());
        return savedCustomer;
    }

    private void isExisted(User user) {
        userRepository.findByEmail(user.getEmail()).ifPresent((it) -> {
            log.error("user already exists with email: {}", it.getEmail());
            throw new UserAlreadyExistsException("user already exists with email: {" + it.getEmail() + "}");
        });

        userRepository.findByUsername(user.getUsername()).ifPresent((it) -> {
            log.error("user already exists with username: {}" + it.getUsername());
            throw new UserAlreadyExistsException("user already exists with username: {" + it.getUsername() + "}");
        });
    }


    @Override
    public Customer findById(Long id) {
        return customerRepository.findById(id).orElseThrow(() -> new UserNotFoundException("user does not exists with id: {" + id + "}"));
    }

    @Override
    public Customer findByUser(User user) {
        return customerRepository.findByUser(user).orElseThrow(() -> new UserNotFoundException("user does not exist"));
    }

    @Override
    public void deleteById(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new UserNotFoundException("customer does not exist: {" + customerId + "}"));

        customerRepository.deleteById(customerId);
        log.info("customer has been deleted: {}", customer.toString());
    }
}
