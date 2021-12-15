package com.shopping.repository;

import com.shopping.model.Cart;
import com.shopping.model.Customer;
import com.shopping.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;
    private Customer customer;
    private Cart cart;

    @BeforeEach
    void setUp() {
        customer = Customer.builder()
                .firstName("John")
                .lastName("Doe")
                .users(new User("email@email.com", "username", "password", true))
                .build();

        cart = Cart.builder()
                .customer(customer)
                .totalPrice(BigDecimal.valueOf(0.00))
                .build();

        customer.setCart(cart);
    }

    @Test
    public void should_save_customer() {
        Customer savedCustomer = customerRepository.saveAndFlush(customer);

        assertNotNull(savedCustomer, "Returned must not be null");
        assertEquals(customer.getFirstName(), savedCustomer.getFirstName(), "First Name must be equal");
    }

    @Test
    public void should_return_list_of_all_customers() {
        customerRepository.save(customer);
        List<Customer> expectedCustomers = customerRepository.findAll();

        assertEquals("John", expectedCustomers.get(0).getFirstName(), "First Name must be equal");
    }
}