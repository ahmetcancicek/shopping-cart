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
    private User user;
    private Customer customer;

    @BeforeEach
    void setUp() {
        user = new User("email@email.com", "username", "password", true);
        customer = new Customer("John", "Doe", user);
    }

    @AfterEach
    void tearDown() {
        customerRepository.deleteAll();
        user = null;
        customer = null;
    }

    @Test
    public void saveCustomer_ValidCustomer_ShouldSaveNewCustomer() {
        customerRepository.save(customer);

        Optional<Customer> fetchedCustomer = customerRepository.findById(customer.getId());

        assertTrue(fetchedCustomer.isPresent());

        assertEquals(customer.getFirstName(), fetchedCustomer.get().getFirstName());
    }

    @Test
    public void getAllCustomers_NoFilter_ShouldReturnListOfAllCustomers() {
        customerRepository.save(customer);

        List<Customer> expectedCustomers = customerRepository.findAll();

        assertEquals("John", expectedCustomers.get(0).getFirstName());
    }
}