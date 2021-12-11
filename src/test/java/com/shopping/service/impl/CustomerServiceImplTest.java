package com.shopping.service.impl;

import com.shopping.model.Cart;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Test
    void should_save_customer() {
        final Customer customer = Customer.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .users(new User("email@email.com", "username", "password", true))
                .build();


        when(customerRepository.saveAndFlush(any(Customer.class))).thenReturn(customer);

        Customer savedCustomer = customerService.save(customer);

        verify(customerRepository, times(1)).saveAndFlush(any());
        assertNotNull(savedCustomer);
        assertEquals(customer.getFirstName(), savedCustomer.getFirstName(), "First name must be equal");
    }

    @Test
    void should_return_cart_id_when_save_customer() {
        final Customer customer = Customer.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .users(new User("email@email.com", "username", "password", true))
                .cart(new Cart())
                .build();

        given(customerRepository.findById(customer.getId())).willReturn(Optional.of(customer));

        final Optional<Customer> expected = customerService.findById(customer.getId());

        assertEquals(customer.getCart().getId(), expected.get().getCart().getId(), "Cart ID must be equal");
    }

    @Test
    void should_return_list_of_all_customers() {
        final User user1 = new User("email@email.com", "username", "password", true);
        final User user2 = new User("email@email.com", "username", "password", true);

        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer(1L, "John", "Doe", user1, null));
        customers.add(new Customer(2L, "July", "Eric", user2, null));

        given(customerRepository.findAll()).willReturn(customers);

        final List<Customer> expectedCustomers = customerService.findAll();

        assertEquals("John", expectedCustomers.get(0).getFirstName(), "First name must be equal");
    }

}