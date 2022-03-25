package com.shopping.service.impl;

import com.shopping.model.*;
import com.shopping.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

        Cart cart = Cart.builder()
                .customer(customer)
                .build();

        customer.setCart(cart);

        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        Customer savedCustomer = customerService.save(customer);

        verify(customerRepository, times(1)).save(any());
        assertEquals(customer.getId(), savedCustomer.getId(), "Id must be equal");
        assertEquals(customer.getFirstName(), savedCustomer.getFirstName(), "First name must be equal");
    }

    @Test
    void should_save_customer_with_payment_and_address() {
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

        Cart cart = Cart.builder()
                .customer(customer)
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

        customer.setCart(cart);
        customer.addPaymentMethod(paymentMethod);
        customer.addAddress(address);


        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        Customer savedCustomer = customerService.save(customer);

        for (PaymentMethod p : savedCustomer.getPaymentMethods()) {
            assertEquals(paymentMethod.getName(), p.getName(), "PaymentMethod name must be equal");
        }

        for (Address a : savedCustomer.getAddresses()) {
            assertEquals(address.getCity(), a.getCity(), "City must be equal");
        }

        verify(customerRepository, times(1)).save(any());
        assertEquals(customer.getId(), savedCustomer.getId(), "Id must be equal");
        assertEquals(customer.getFirstName(), savedCustomer.getFirstName(), "First name must be equal");

    }

    @Test
    public void should_throw_exception_when_save_customer_with_existing_email() {

    }

    @Test
    public void should_throw_exception_when_save_customer_with_existing_username() {

    }


    @Test
    void should_return_cart_id_when_save_customer() {
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

        Cart cart = Cart.builder()
                .id(1L)
                .customer(customer)
                .build();

        customer.setCart(cart);

        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        Customer savedCustomer = customerService.save(customer);

        verify(customerRepository, times(1)).save(any());
        assertNotNull(customer.getCart().getId(), "CartId must not be null");
        assertEquals(customer.getFirstName(), savedCustomer.getFirstName(), "First Name must be equal");
        assertEquals(customer.getCart().getId(), savedCustomer.getCart().getId(), "Cart ID must be equal");
    }

    @Test
    void should_return_list_of_all_customers() {
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

        given(customerRepository.findAll()).willReturn(customers);

        final List<Customer> expectedCustomers = customerService.findAll();

        verify(customerRepository, times(1)).findAll();
        assertEquals(customerOne.getFirstName(), expectedCustomers.get(0).getFirstName(), "First Name must be equal");
        assertEquals(customerTwo.getFirstName(), expectedCustomers.get(1).getFirstName(), "First Name must be equal");
    }

    @Test
    void should_return_customer_when_called_findById() {
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

        given(customerRepository.findById(customer.getId())).willReturn(Optional.of(customer));

        Optional<Customer> expected = customerService.findById(customer.getId());

        verify(customerRepository, times(1)).findById(any());
        assertTrue(expected.isPresent(), "Returned must not be null");
        assertEquals(customer.getFirstName(), expected.get().getFirstName(), "Firstname must be equal");
    }

    @Test
    void should_return_customer_when_called_findByUser() {
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

        given(customerRepository.findByUser(customer.getUser())).willReturn(Optional.of(customer));

        Optional<Customer> expected = customerService.findByUser(customer.getUser());

        verify(customerRepository, times(1)).findByUser(any());
        assertTrue(expected.isPresent(), "Returned must not be null");
        assertEquals(customer.getFirstName(), expected.get().getFirstName(), "Firstname mut be equal");
    }


}