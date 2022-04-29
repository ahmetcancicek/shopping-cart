package com.shopping.service.impl;

import com.shopping.exception.AlreadyExistsElementException;
import com.shopping.exception.NoSuchElementFoundException;
import com.shopping.mapper.CustomerMapper;
import com.shopping.model.*;
import com.shopping.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @Test
    void it_should_save_customer() {
        // given
        Customer customer = Customer.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .user(User.builder()
                        .id(1L)
                        .email("email@email.com")
                        .username("username")
                        .password("password")
                        .active(true)
                        .build())
                .build();

        given(customerRepository.save(any())).willReturn(customer);

        // when
        Customer savedCustomer = CustomerMapper.INSTANCE.toCustomer(
                customerService.save(CustomerMapper.INSTANCE.toCustomerPayload(customer))
        );

        // then
        verify(customerRepository, times(1)).save(any());
        assertNotNull(savedCustomer, "Entity must not be null");
        assertEquals(customer.getFirstName(), savedCustomer.getFirstName(), "First name must be equal");
    }

    @Test
    void it_should_delete_customer() {
        // given
        Customer customer = Customer.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .user(User.builder()
                        .email("email@email.com")
                        .username("username")
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
    void it_should_throw_exception_when_delete_customer() {
        // when
        Throwable throwable = catchThrowable(() -> {
            customerService.deleteById(1L);
        });

        // then
        assertThat(throwable).isInstanceOf(NoSuchElementFoundException.class);
    }

    @Test
    void it_should_save_customer_with_payment_and_address() {
        // given
        Customer customer = Customer.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .user(User.builder()
                        .id(1L)
                        .email("email@email.com")
                        .username("username")
                        .password("password")
                        .active(true)
                        .build())
                .build();

        customer.addPaymentMethod(PaymentMethod.builder()
                .name("My PayPal Account")
                .paymentType(PaymentType.PAYPAL)
                .build());

        customer.addAddress(Address.builder()
                .city("New York")
                .zipCode("34000")
                .stateCode("01")
                .zipCode("0101")
                .street("XY56Y")
                .build());

        given(customerRepository.save(any())).willReturn(customer);

        // when
        Customer savedCustomer = CustomerMapper.INSTANCE.toCustomer(
                customerService.save(CustomerMapper.INSTANCE.toCustomerPayload(customer))
        );

        // then
        verify(customerRepository, times(1)).save(any());
        assertNotNull(savedCustomer, "Saved customer must not be null");
        assertEquals(customer.getFirstName(), savedCustomer.getFirstName(), "First name must be equal");
        assertNotNull(customer.getAddresses(), "Address must not be null");
        assertNotNull(customer.getPaymentMethods(), "Payment Methods must not be null");
    }

    @Test
    public void it_should_throw_exception_when_save_customer_with_existing_email() {
        // given
        Customer customer = Customer.builder()
                .firstName("John")
                .lastName("Doe")
                .user(User.builder()
                        .email("email@email.com")
                        .username("username")
                        .password("password")
                        .active(true)
                        .build())
                .build();

        given(customerRepository.findByUser_Email(any())).willReturn(Optional.of(customer));

        // when
        Throwable throwable = catchThrowable(() -> {
            Customer savedCustomer = CustomerMapper.INSTANCE.toCustomer(
                    customerService.save(CustomerMapper.INSTANCE.toCustomerPayload(customer))
            );
        });

        // then
        assertThat(throwable).isInstanceOf(AlreadyExistsElementException.class);
    }

    @Test
    public void it_should_throw_exception_when_save_customer_with_existing_username() {
        // given
        Customer customer = Customer.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .user(User.builder()
                        .id(1L)
                        .email("email@email.com")
                        .username("username")
                        .password("password")
                        .active(true)
                        .build())
                .build();

        given(customerRepository.findByUser_Username(any())).willReturn(Optional.of(customer));

        // when
        Throwable throwable = catchThrowable(() -> {
            Customer savedCustomer = CustomerMapper.INSTANCE.toCustomer(
                    customerService.save(CustomerMapper.INSTANCE.toCustomerPayload(customer))
            );
        });

        // then
        assertThat(throwable).isInstanceOf(AlreadyExistsElementException.class);
    }

    @Test
    void it_should_return_list_of_all_customers() {
        // given
        List<Customer> customers = new ArrayList<>();
        customers.add(Customer.builder()
                .firstName("John")
                .lastName("Doe")
                .user(User.builder()
                        .email("email@email.com")
                        .username("username")
                        .password("password")
                        .active(true)
                        .build())
                .build());

        customers.add(Customer.builder()
                .firstName("July")
                .lastName("Eric")
                .user(User.builder()
                        .email("email@email.com")
                        .username("username")
                        .password("password")
                        .active(true)
                        .build())
                .build());

        given(customerRepository.findAll()).willReturn(customers);

        // when
        List<Customer> expectedCustomers = CustomerMapper.INSTANCE.toCustomers(customerService.findAll());

        // then
        verify(customerRepository, times(1)).findAll();
        assertNotNull(expectedCustomers, "List must not be null");
        assertEquals("John", expectedCustomers.get(0).getFirstName(), "First Name must be equal");
        assertEquals("July", expectedCustomers.get(1).getFirstName(), "First Name must be equal");
    }

    @Test
    void it_should_return_customer_of_that_id() {
        // given
        Customer customer = Customer.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .user(User.builder()
                        .email("email@email.com")
                        .username("username")
                        .password("password")
                        .active(true)
                        .build())
                .cart(new Cart())
                .build();

        given(customerRepository.findById(any())).willReturn(Optional.of(customer));

        // when
        Customer expectedCustomer = CustomerMapper.INSTANCE.toCustomer(customerService.findById(customer.getId()));

        // then
        verify(customerRepository, times(1)).findById(any());
        assertNotNull(expectedCustomer, "Entity must not be null");
        assertEquals(customer.getFirstName(), expectedCustomer.getFirstName(), "Firstname must be equal");
    }

    @Test
    void it_should_return_customer_of_that_user() {
        // given
        Customer customer = Customer.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .user(User.builder()
                        .id(1L)
                        .username("username")
                        .password("password")
                        .email("email@email.com")
                        .active(true)
                        .build())
                .cart(new Cart())
                .build();

        given(customerRepository.findByUser(any())).willReturn(Optional.of(customer));

        // when
        Customer expectedCustomer = CustomerMapper.INSTANCE.toCustomer(customerService.findByUser(customer.getUser()));

        // then
        verify(customerRepository, times(1)).findByUser(any());
        assertNotNull(expectedCustomer, "Entity must not be null");
        assertEquals(customer.getFirstName(), expectedCustomer.getFirstName(), "Firstname mut be equal");
    }
}