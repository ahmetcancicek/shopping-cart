package com.shopping.service.impl;

import com.shopping.domain.dto.CustomerResponse;
import com.shopping.domain.model.Cart;
import com.shopping.domain.model.Customer;
import com.shopping.domain.model.Role;
import com.shopping.domain.model.User;
import com.shopping.domain.exception.AlreadyExistsElementException;
import com.shopping.domain.exception.NoSuchElementFoundException;
import com.shopping.domain.mapper.CustomerMapper;
import com.shopping.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
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

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private RoleServiceImpl roleService;

    @Test
    public void it_should_save_customer() {
        // given
        Customer customer = Customer.builder()
                .id(1L)
                .firstName("Bill")
                .lastName("King")
                .user(User.builder()
                        .id(1L)
                        .email("billking@email.com")
                        .username("billking")
                        .password("password")
                        .active(true)
                        .build())
                .build();

        Role role = Role.builder()
                .name("USER")
                .build();

        given(customerRepository.save(any())).willReturn(customer);
        given(roleService.findByName(any())).willReturn(role);

        // when
        CustomerResponse savedCustomer = customerService.save(CustomerMapper.INSTANCE.toCustomerRequestFromCustomer(customer));

        // then
        verify(customerRepository, times(1)).save(any());
        assertNotNull(savedCustomer, "Returned must not be null");
        assertEquals(customer.getFirstName(), savedCustomer.getFirstName(), "First name must be equal");
        assertEquals(customer.getLastName(), savedCustomer.getLastName(), "Last name must be equal");
        assertEquals(customer.getUser().getEmail(), savedCustomer.getEmail(), "Email must be equal");
    }

    @Test
    public void it_should_delete_customer_of_that_id() {
        // given
        Customer customer = Customer.builder()
                .id(1L)
                .firstName("Bill")
                .lastName("King")
                .user(User.builder()
                        .email("billking@email.com")
                        .username("billking")
                        .password("password")
                        .active(true)
                        .build())
                .build();

        given(customerRepository.findById(any())).willReturn(Optional.of(customer));

        // when
        customerService.deleteById(1L);

        // then
        verify(customerRepository, times(1)).deleteById(any());
    }

    @Test
    public void it_should_delete_customer_of_that_username() {
        // given
        Customer customer = Customer.builder()
                .id(1L)
                .firstName("Bill")
                .lastName("King")
                .user(User.builder()
                        .email("billking@email.com")
                        .username("billking")
                        .password("AD^32fN")
                        .active(true)
                        .build())
                .build();

        given(customerRepository.findByUser_Username(any())).willReturn(Optional.of(customer));

        // when
        customerService.deleteByUsername("billking");

        // then
        verify(customerRepository, times(1)).deleteByUser_Username("billking");
    }

    @Test
    void it_should_throw_exception_when_delete_customer_of_that_id() {
        // when
        Throwable throwable = catchThrowable(() -> {
            customerService.deleteById(1L);
        });

        // then
        assertThat(throwable).isInstanceOf(NoSuchElementFoundException.class);
    }

    @Test
    void it_should_throw_exception_when_delete_customer_of_that_username() {
        // when
        Throwable throwable = catchThrowable(() -> {
            customerService.deleteByUsername("billking");
        });

        // then
        assertThat(throwable).isInstanceOf(NoSuchElementFoundException.class);
    }

    @Test
    public void it_should_throw_exception_when_save_customer_with_existing_email() {
        // given
        Customer customer = Customer.builder()
                .firstName("Bill")
                .lastName("King")
                .user(User.builder()
                        .email("billking@email.com")
                        .username("billking")
                        .password("password")
                        .active(true)
                        .build())
                .build();

        given(customerRepository.findByUser_Email(any())).willReturn(Optional.of(customer));

        // when
        Throwable throwable = catchThrowable(() -> {
            CustomerResponse savedCustomer = customerService.save(CustomerMapper.INSTANCE.toCustomerRequestFromCustomer(customer));
        });

        // then
        verify(customerRepository, times(1)).findByUser_Email(any());
        assertThat(throwable).isInstanceOf(AlreadyExistsElementException.class);
    }

    @Test
    public void it_should_throw_exception_when_save_customer_with_existing_username() {
        // given
        Customer customer = Customer.builder()
                .id(1L)
                .firstName("Bill")
                .lastName("King")
                .user(User.builder()
                        .id(1L)
                        .email("billking@email.com")
                        .username("billking")
                        .password("password")
                        .active(true)
                        .build())
                .build();

        given(customerRepository.findByUser_Username(any())).willReturn(Optional.of(customer));

        // when
        Throwable throwable = catchThrowable(() -> {
            CustomerResponse savedCustomer = customerService.save(CustomerMapper.INSTANCE.toCustomerRequestFromCustomer(customer));
        });

        // then
        verify(customerRepository, times(1)).findByUser_Username(any());
        assertThat(throwable).isInstanceOf(AlreadyExistsElementException.class);
    }

    @Test
    public void it_should_return_list_of_all_customers() {
        // given
        List<Customer> customers = new ArrayList<>();
        customers.add(Customer.builder()
                .firstName("Bill")
                .lastName("King")
                .user(User.builder()
                        .email("billking@email.com")
                        .username("billking")
                        .password("password")
                        .active(true)
                        .build())
                .build());

        customers.add(Customer.builder()
                .firstName("July")
                .lastName("Eric")
                .user(User.builder()
                        .email("julyeric@email.com")
                        .username("julyeric")
                        .password("password")
                        .active(true)
                        .build())
                .build());

        given(customerRepository.findAll()).willReturn(customers);

        // when
        List<CustomerResponse> expectedCustomers = customerService.findAll();

        // then
        verify(customerRepository, times(1)).findAll();
        assertNotNull(expectedCustomers, "List must not be null");
        assertEquals("Bill", expectedCustomers.get(0).getFirstName(), "First Name must be equal");
        assertEquals("King", expectedCustomers.get(0).getLastName(), "Last Name must be equal");
        assertEquals("July", expectedCustomers.get(1).getFirstName(), "First Name must be equal");
        assertEquals("Eric", expectedCustomers.get(1).getLastName(), "Last Name must be equal");

    }

    @Test
    public void it_should_return_customer_of_that_id() {
        // given
        Customer customer = Customer.builder()
                .id(1L)
                .firstName("Bill")
                .lastName("King")
                .user(User.builder()
                        .email("billking@email.com")
                        .username("billking")
                        .password("password")
                        .active(true)
                        .build())
                .cart(new Cart())
                .build();

        given(customerRepository.findById(any())).willReturn(Optional.of(customer));

        // when
        CustomerResponse expectedCustomer = customerService.findById(customer.getId());

        // then
        verify(customerRepository, times(1)).findById(any());
        assertNotNull(expectedCustomer, "Returned must not be null");
        assertEquals(customer.getFirstName(), expectedCustomer.getFirstName(), "Firstname must be equal");
        assertEquals(customer.getLastName(), expectedCustomer.getLastName(), "Last name must be equal");
        assertEquals(customer.getUser().getEmail(), expectedCustomer.getEmail(), "Email must be equal");
        assertEquals(customer.getUser().getUsername(), expectedCustomer.getUsername(), "Username must be equal");

    }

    @Test
    public void it_should_return_customer_of_that_user() {
        // given
        Customer customer = Customer.builder()
                .id(1L)
                .firstName("Bill")
                .lastName("King")
                .user(User.builder()
                        .id(1L)
                        .username("billking")
                        .password("password")
                        .email("billking@email.com")
                        .active(true)
                        .build())
                .cart(new Cart())
                .build();

        given(customerRepository.findByUser(any())).willReturn(Optional.of(customer));

        // when
        CustomerResponse expectedCustomer = customerService.findByUser(customer.getUser());

        // then
        verify(customerRepository, times(1)).findByUser(any());
        assertNotNull(expectedCustomer, "Returned must not be null");
        assertEquals(customer.getFirstName(), expectedCustomer.getFirstName(), "Firstname must be equal");
        assertEquals(customer.getLastName(), expectedCustomer.getLastName(), "Last name must be equal");
        assertEquals(customer.getUser().getEmail(), expectedCustomer.getEmail(), "Email must be equal");
        assertEquals(customer.getUser().getUsername(), expectedCustomer.getUsername(), "Username must be equal");
    }

    @Test
    public void it_should_return_customer_of_that_email() {
        // given
        Customer customer = Customer.builder()
                .id(1L)
                .firstName("Bill")
                .lastName("King")
                .user(User.builder()
                        .id(1L)
                        .username("billking")
                        .password("password")
                        .email("billking@email.com")
                        .active(true)
                        .build())
                .cart(new Cart())
                .build();

        given(customerRepository.findByUser_Email(any())).willReturn(Optional.of(customer));

        // when
        CustomerResponse expectedCustomer = customerService.findByEmail(customer.getUser().getEmail());

        // then
        verify(customerRepository, times(1)).findByUser_Email(any());
        assertNotNull(expectedCustomer, "Returned must not be null");
        assertEquals(customer.getFirstName(), expectedCustomer.getFirstName(), "Firstname must be equal");
        assertEquals(customer.getLastName(), expectedCustomer.getLastName(), "Last name must be equal");
        assertEquals(customer.getUser().getEmail(), expectedCustomer.getEmail(), "Email must be equal");
        assertEquals(customer.getUser().getUsername(), expectedCustomer.getUsername(), "Username must be equal");
    }

    @Test
    public void it_should_return_customer_of_that_username() {
        // given
        Customer customer = Customer.builder()
                .id(1L)
                .firstName("Bill")
                .lastName("King")
                .user(User.builder()
                        .id(1L)
                        .username("billking")
                        .password("password")
                        .email("billking@email.com")
                        .active(true)
                        .build())
                .cart(new Cart())
                .build();

        given(customerRepository.findByUser_Username(any())).willReturn(Optional.of(customer));

        // when
        CustomerResponse expectedCustomer = customerService.findByUsername(customer.getUser().getUsername());

        // then
        verify(customerRepository, times(1)).findByUser_Username(any());
        assertNotNull(expectedCustomer, "Returned must not be null");
        assertEquals(customer.getFirstName(), expectedCustomer.getFirstName(), "Firstname must be equal");
        assertEquals(customer.getLastName(), expectedCustomer.getLastName(), "Last name must be equal");
        assertEquals(customer.getUser().getEmail(), expectedCustomer.getEmail(), "Email must be equal");
        assertEquals(customer.getUser().getUsername(), expectedCustomer.getUsername(), "Username must be equal");
    }

    @Test
    public void it_should_return_customer_of_that_username_without_payload() {
        // given
        Customer customer = Customer.builder()
                .id(1L)
                .firstName("Bill")
                .lastName("King")
                .user(User.builder()
                        .id(1L)
                        .username("billking")
                        .password("password")
                        .email("billking@email.com")
                        .active(true)
                        .build())
                .cart(new Cart())
                .build();

        given(customerRepository.findByUser_Username(any())).willReturn(Optional.of(customer));

        // when
        Customer expectedCustomer = customerService.findCustomerByUsername(customer.getUser().getUsername());

        // then
        verify(customerRepository, times(1)).findByUser_Username(any());
        assertNotNull(expectedCustomer, "Returned must not be null");
        assertEquals(customer.getFirstName(), expectedCustomer.getFirstName(), "Firstname must be equal");
        assertEquals(customer.getLastName(), expectedCustomer.getLastName(), "Last name must be equal");
        assertEquals(customer.getUser(), expectedCustomer.getUser(), "Email must be equal");
    }
}