package com.shopping.repository;

import com.shopping.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void should_save_customer() {
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

        customer.setCart(cart);

        Set<ConstraintViolation<Customer>> violationSet = validator.validate(customer);
        assertTrue(violationSet.isEmpty(), "Entity must be validate");

        Customer savedCustomer = customerRepository.save(customer);

        assertNotNull(savedCustomer, "Returned must not be null");
        assertEquals(customer.getId(), savedCustomer.getId(), "Id must be equal");
        assertEquals(customer.getFirstName(), savedCustomer.getFirstName(), "First Name must be equal");
        assertNotNull(savedCustomer.getCart().getId(), "CartId must not be null");
    }

    @Test
    public void should_save_customer_with_payment_and_address() {
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

        Set<ConstraintViolation<Customer>> violationSet = validator.validate(customer);
        assertTrue(violationSet.isEmpty(), "Entity must be validate");

        Customer savedCustomer = customerRepository.save(customer);

        for (PaymentMethod p : savedCustomer.getPaymentMethods()) {
            assertEquals(paymentMethod.getName(),p.getName(), "PaymentMethod name must be equal");
        }

        for (Address a : savedCustomer.getAddresses()) {
            assertEquals(address.getCity(), a.getCity(), "City must be equal");
        }

        assertNotNull(savedCustomer, "Returned must not be null");
        assertEquals(customer.getId(), savedCustomer.getId(), "Id must be equal");
        assertEquals(customer.getFirstName(), savedCustomer.getFirstName(), "First Name must be equal");
        assertNotNull(savedCustomer.getCart().getId(), "CartId must not be null");
    }

    @Test
    public void should_throw_exception_when_save_customer_with_existing_email(){

    }

    @Test
    public void should_throw_exception_when_save_customer_with_existing_username(){

    }

    @Test
    public void should_return_list_of_all_customers() {
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

        customerRepository.save(customer);
        List<Customer> expectedCustomers = customerRepository.findAll();

        assertEquals(customer.getId(), expectedCustomers.get(0).getId(), "Id must be equal");
        assertEquals(customer.getFirstName(), expectedCustomers.get(0).getFirstName(), "First Name must be equal");
    }
}