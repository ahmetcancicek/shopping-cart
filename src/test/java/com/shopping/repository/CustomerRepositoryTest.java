package com.shopping.repository;

import com.shopping.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private CustomerRepository customerRepository;

    @Container
    public static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql");

    @Test
    public void it_should_db_run() {
        assertTrue(mysql.isRunning(), "MySQL is not running");
    }

    @Test
    public void it_should_save_customer() {
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

        Customer savedCustomer = customerRepository.save(customer);
        Customer expectedCustomer = testEntityManager.find(Customer.class, savedCustomer.getId());

        assertEquals("John", expectedCustomer.getFirstName());

        testEntityManager.remove(customer);
        testEntityManager.flush();
    }

    @Test
    public void it_should_delete_customer() {
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

        Object customerId = testEntityManager.persistAndGetId(customer);
        customerRepository.deleteById((Long) customerId);

        assertNull(testEntityManager.find(Customer.class, customerId),"Returned must be null");
    }

    @Test
    public void it_should_save_customer_with_payment_and_address() {
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

        Customer savedCustomer = customerRepository.save(customer);
        Customer expectedCustomer = testEntityManager.find(Customer.class, savedCustomer.getId());

        assertEquals(1, expectedCustomer.getPaymentMethods().size());
        assertEquals(1, expectedCustomer.getAddresses().size());

        testEntityManager.remove(customer);
        testEntityManager.flush();
    }

    @Test
    public void it_should_throw_exception_when_save_customer_with_existing_email() {
        User userOne = User.builder()
                .email("email@email.com")
                .username("usernameOne")
                .password("password")
                .active(true)
                .build();

        Customer customerOne = Customer.builder()
                .firstName("John")
                .lastName("Doe")
                .user(userOne)
                .build();

        testEntityManager.persistAndFlush(customerOne);


        User userTwo = User.builder()
                .email("email@email.com")
                .username("usernameTwo")
                .password("password")
                .active(true)
                .build();

        Customer customerTwo = Customer.builder()
                .firstName("Lucy")
                .lastName("Doe")
                .user(userTwo)
                .build();

        Throwable throwable = catchThrowable(() -> {
            customerRepository.saveAndFlush(customerTwo);
        });

        assertThat(throwable).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    public void it_should_throw_exception_when_save_customer_with_existing_username() {
        User userOne = User.builder()
                .email("emailOne@email.com")
                .username("username")
                .password("password")
                .active(true)
                .build();

        Customer customerOne = Customer.builder()
                .firstName("John")
                .lastName("Doe")
                .user(userOne)
                .build();

        testEntityManager.persistAndFlush(customerOne);


        User userTwo = User.builder()
                .email("emailTwo@email.com")
                .username("username")
                .password("password")
                .active(true)
                .build();

        Customer customerTwo = Customer.builder()
                .firstName("Lucy")
                .lastName("Doe")
                .user(userTwo)
                .build();

        Throwable throwable = catchThrowable(() -> {
            customerRepository.saveAndFlush(customerTwo);
        });

        assertThat(throwable).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    public void it_should_return_list_of_all_customers() {
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

        testEntityManager.persistAndFlush(customer);

        assertEquals(1, customerRepository.findAll().size());

        testEntityManager.remove(customer);
        testEntityManager.flush();
    }

    @Test
    public void it_should_return_customer_of_that_id() {
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

        Object id = testEntityManager.persistAndGetId(customer);

        Optional<Customer> expectedCustomer = customerRepository.findById((Long) id);

        assertTrue(expectedCustomer.isPresent(), "Returned must not be null");
        assertEquals("John", expectedCustomer.get().getFirstName(), "First Name must be equal");

        testEntityManager.remove(customer);
        testEntityManager.flush();
    }

    @Test
    public void it_should_return_customer_of_that_user() {
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

        testEntityManager.persistAndFlush(customer);

        Optional<Customer> expectedCustomer = customerRepository.findByUser(user);

        assertTrue(expectedCustomer.isPresent(), "Returned must not be null");
        assertEquals("John", expectedCustomer.get().getFirstName(), "First Name must be equal");

        testEntityManager.remove(customer);
        testEntityManager.flush();
    }

    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.jpa.hibernate.dll-auto", () -> "create-drop");
        registry.add("spring.jpa.properties.hibernate.dialect", () -> "org.hibernate.dialect.MySQL8Dialect");
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }
}