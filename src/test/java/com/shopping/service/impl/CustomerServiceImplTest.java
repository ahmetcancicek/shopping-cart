package com.shopping.service.impl;

import com.shopping.model.Customer;
import com.shopping.model.User;
import com.shopping.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Test
    void it_should_save_customer() {
        final User user = User.builder()
                .email("email@email.com")
                .username("username")
                .password("password")
                .active(true)
                .build();

        final Customer customer = Customer.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .users(user)
                .build();

        given(customerRepository.findById(customer.getId())).willReturn(Optional.of(customer));

        final Optional<Customer> expected = customerService.findById(customer.getId());

        assertTrue(expected.isPresent());

        assertEquals(customer.getFirstName(), expected.get().getFirstName());
    }


    @Test
    void it_should_return_list_of_all_customers() {
        final User user1 = new User("email@email.com", "username", "password", true);
        final User user2 = new User("email@email.com", "username", "password", true);

        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer(1L, "John", "Doe", user1));
        customers.add(new Customer(2L, "July", "Eric", user2));

        given(customerRepository.findAll()).willReturn(customers);

        final List<Customer> expectedCustomers = customerService.findAll();

        assertEquals("John", expectedCustomers.get(0).getFirstName());
    }

}