package com.shopping.repository;

import com.shopping.model.Customer;
import com.shopping.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;
    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = Customer.builder()
                .firstName("John")
                .lastName("Doe")
                .users(new User("email@email.com", "username", "password", true))
                .build();
    }

    @AfterEach
    void tearDown() {
        customerRepository.deleteAll();
        customer = null;
    }

    @Test
    public void should_save_customer() {
        customerRepository.save(customer);

        Optional<Customer> fetchedCustomer = customerRepository.findById(customer.getId());

        assertTrue(fetchedCustomer.isPresent());

        assertEquals(customer.getFirstName(), fetchedCustomer.get().getFirstName());
    }

    @Test
    public void should_return_list_of_all_customers() {
        customerRepository.save(customer);

        List<Customer> expectedCustomers = customerRepository.findAll();

        assertEquals("John", expectedCustomers.get(0).getFirstName());
    }
}