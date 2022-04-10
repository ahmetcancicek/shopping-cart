package com.shopping.service.impl;

import com.shopping.exception.UserAlreadyExistsException;
import com.shopping.model.*;
import com.shopping.repository.CustomerRepository;
import com.shopping.repository.UserRepository;
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

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Test
    void it_should_save_customer() {
        // given
        User user = User.builder()
                .email("email@email.com")
                .username("username")
                .password("password")
                .active(true)
                .build();

        Customer customer = Customer.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .user(user)
                .build();

        when(customerRepository.save(any())).thenReturn(customer);

        // when
        Customer savedCustomer = customerService.save(customer);

        // then
        verify(customerRepository, times(1)).save(any());
        assertNotNull(savedCustomer, "Entity must not be null");
        assertEquals(customer.getId(), savedCustomer.getId(), "Id must be equal");
        assertEquals(customer.getFirstName(), savedCustomer.getFirstName(), "First name must be equal");
    }

    @Test
    void it_should_save_customer_with_payment_and_address() {
        // given
        User user = User.builder()
                .email("email@email.com")
                .username("username")
                .password("password")
                .active(true)
                .build();

        Customer customer = Customer.builder()
                .firstName("John")
                .lastName("Doe")
                .user(user)
                .build();

        PaymentMethod paymentMethod = PaymentMethod.builder()
                .name("My PayPal Account")
                .paymentType(PaymentType.PAYPAL)
                .build();

        Address address = Address.builder()
                .city("New York")
                .zipCode("34000")
                .stateCode("01")
                .zipCode("0101")
                .street("XY56Y")
                .build();

        customer.addPaymentMethod(paymentMethod);
        customer.addAddress(address);

        when(customerRepository.save(any())).thenReturn(customer);

        // when
        Customer savedCustomer = customerService.save(customer);

        // then
        verify(customerRepository, times(1)).save(any());
        assertNotNull(savedCustomer, "Saved customer must not be null");
        assertEquals(customer.getId(), savedCustomer.getId(), "Id must be equal");
        assertEquals(customer.getFirstName(), savedCustomer.getFirstName(), "First name must be equal");
        assertNotNull(customer.getAddresses(), "Address must not be null");
        assertNotNull(customer.getPaymentMethods(), "Payment Methods must not be null");
    }

    @Test
    public void it_should_throw_exception_when_save_customer_with_existing_email() {
        // given
        User user = User.builder()
                .email("email@email.com")
                .username("username")
                .password("password")
                .active(true)
                .build();

        Customer customer = Customer.builder()
                .firstName("John")
                .lastName("Doe")
                .user(user)
                .build();

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));

        // when
        Throwable throwable = catchThrowable(() -> {
            customerService.save(customer);
        });

        // then
        assertThat(throwable).isInstanceOf(UserAlreadyExistsException.class);
        verifyNoInteractions(customerRepository);

    }

    @Test
    public void it_should_throw_exception_when_save_customer_with_existing_username() {
        // given
        User user = User.builder()
                .email("email@email.com")
                .username("username")
                .password("password")
                .active(true)
                .build();

        Customer customer = Customer.builder()
                .firstName("John")
                .lastName("Doe")
                .user(user)
                .build();

        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));

        // when
        Throwable throwable = catchThrowable(() -> {
            customerService.save(customer);
        });

        // then
        assertThat(throwable).isInstanceOf(UserAlreadyExistsException.class);
        verifyNoInteractions(customerRepository);
    }


    @Test
    void it_should_return_cart_id_when_save_customer() {
        // given
        User user = User.builder()
                .email("email@email.com")
                .username("username")
                .password("password")
                .active(true)
                .build();

        Customer customer = Customer.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .user(user)
                .cart(null)
                .build();

        // when
        when(customerRepository.save(any())).thenReturn(customer);
        Customer savedCustomer = customerService.save(customer);

        // then
        verify(customerRepository, times(1)).save(any());
        assertNotNull(savedCustomer, "Saved customer must not be null");
        assertNotNull(customer.getCart(), "Cart must not be null");
        assertEquals(customer.getFirstName(), savedCustomer.getFirstName(), "First Name must be equal");
    }

    @Test
    void it_should_return_list_of_all_customers() {
        // given
        User user = User.builder()
                .email("email@email.com")
                .username("username")
                .password("password")
                .active(true)
                .build();

        Customer customerOne = Customer.builder()
                .firstName("John")
                .lastName("Doe")
                .user(user)
                .build();

        Customer customerTwo = Customer.builder()
                .firstName("July")
                .lastName("Eric")
                .user(user)
                .build();

        List<Customer> customers = new ArrayList<>();
        customers.add(customerOne);
        customers.add(customerTwo);

        // when
        given(customerRepository.findAll()).willReturn(customers);
        final List<Customer> expectedCustomers = customerService.findAll();

        // then
        verify(customerRepository, times(1)).findAll();
        assertNotNull(expectedCustomers, "List must not be null");
        assertEquals(customerOne.getFirstName(), expectedCustomers.get(0).getFirstName(), "First Name must be equal");
        assertEquals(customerTwo.getFirstName(), expectedCustomers.get(1).getFirstName(), "First Name must be equal");
    }

    @Test
    void it_should_return_customer_of_that_id() {
        // given
        User user = User.builder()
                .email("email@email.com")
                .username("username")
                .password("password")
                .active(true)
                .build();

        Customer customer = Customer.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .user(user)
                .cart(new Cart())
                .build();

        given(customerRepository.findById(any())).willReturn(Optional.of(customer));

        // when
        Optional<Customer> expectedCustomer = customerService.findById(customer.getId());

        // then
        verify(customerRepository, times(1)).findById(any());
        assertNotNull(expectedCustomer, "Entity must not be null");
        assertTrue(expectedCustomer.isPresent(), "Returned must not be null");
        assertEquals(customer.getFirstName(), expectedCustomer.get().getFirstName(), "Firstname must be equal");
    }

    @Test
    void it_should_return_customer_of_that_user() {
        // given
        User user = User.builder()
                .id(1L)
                .username("username")
                .password("password")
                .email("email@email.com")
                .active(true)
                .build();

        Customer customer = Customer.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .user(user)
                .cart(new Cart())
                .build();

        given(customerRepository.findByUser(any())).willReturn(Optional.of(customer));

        // when
        Optional<Customer> expectedCustomer = customerService.findByUser(customer.getUser());

        // then
        verify(customerRepository, times(1)).findByUser(any());
        assertNotNull(expectedCustomer, "Entity must not be null");
        assertTrue(expectedCustomer.isPresent(), "Returned must not be null");
        assertEquals(customer.getFirstName(), expectedCustomer.get().getFirstName(), "Firstname mut be equal");
    }
}