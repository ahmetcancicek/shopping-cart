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

        // when
        Customer savedCustomer = customerRepository.save(customer);
        Customer expectedCustomer = testEntityManager.find(Customer.class, savedCustomer.getId());

        // then
        assertEquals("John", expectedCustomer.getFirstName());

        testEntityManager.remove(customer);
        testEntityManager.flush();
    }

    @Test
    public void it_should_delete_customer_of_that_id() {
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

        Object customerId = testEntityManager.persistAndGetId(customer);

        // when
        customerRepository.deleteById((Long) customerId);

        // then
        assertNull(testEntityManager.find(Customer.class, customerId), "Returned must be null");
    }

    @Test
    public void it_should_delete_customer_of_that_username() {
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

        Object customerId = testEntityManager.persistAndGetId(customer);

        // when
        customerRepository.deleteByUser_Username(customer.getUser().getUsername());

        // then
        assertNull(testEntityManager.find(Customer.class, customerId), "Returned must be null");
    }

    @Test
    public void it_should_save_customer_with_payment_and_address() {
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

        // when
        Customer savedCustomer = customerRepository.save(customer);
        Customer expectedCustomer = testEntityManager.find(Customer.class, savedCustomer.getId());

        // then
        assertEquals(1, expectedCustomer.getPaymentMethods().size());
        assertEquals(1, expectedCustomer.getAddresses().size());

        testEntityManager.remove(customer);
        testEntityManager.flush();
    }

    @Test
    public void it_should_throw_exception_when_save_customer_with_existing_email() {
        // given
        Customer customerOne = Customer.builder()
                .firstName("John")
                .lastName("Doe")
                .user(User.builder()
                        .email("email@email.com")
                        .username("usernameOne")
                        .password("password")
                        .active(true)
                        .build())
                .build();

        testEntityManager.persistAndFlush(customerOne);

        Customer customerTwo = Customer.builder()
                .firstName("Lucy")
                .lastName("Doe")
                .user(User.builder()
                        .email("email@email.com")
                        .username("usernameTwo")
                        .password("password")
                        .active(true)
                        .build())
                .build();

        // when
        Throwable throwable = catchThrowable(() -> {
            customerRepository.saveAndFlush(customerTwo);
        });

        // then
        assertThat(throwable).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    public void it_should_throw_exception_when_save_customer_with_existing_username() {
        // given
        Customer customerOne = Customer.builder()
                .firstName("John")
                .lastName("Doe")
                .user(User.builder()
                        .email("emailOne@email.com")
                        .username("username")
                        .password("password")
                        .active(true)
                        .build())
                .build();

        testEntityManager.persistAndFlush(customerOne);

        Customer customerTwo = Customer.builder()
                .firstName("Lucy")
                .lastName("Doe")
                .user(User.builder()
                        .email("emailTwo@email.com")
                        .username("username")
                        .password("password")
                        .active(true)
                        .build())
                .build();

        // when
        Throwable throwable = catchThrowable(() -> {
            customerRepository.saveAndFlush(customerTwo);
        });

        // then
        assertThat(throwable).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    public void it_should_return_list_of_all_customers() {
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

        // when
        testEntityManager.persistAndFlush(customer);

        // then
        assertEquals(1, customerRepository.findAll().size());

        testEntityManager.remove(customer);
        testEntityManager.flush();
    }

    @Test
    public void it_should_return_customer_of_that_id() {
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

        Object id = testEntityManager.persistAndGetId(customer);

        // when
        Optional<Customer> expectedCustomer = customerRepository.findById((Long) id);

        // then
        assertTrue(expectedCustomer.isPresent(), "Returned must not be null");
        assertEquals("John", expectedCustomer.get().getFirstName(), "First Name must be equal");

        testEntityManager.remove(customer);
        testEntityManager.flush();
    }

    @Test
    public void it_should_return_customer_of_that_user() {
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

        testEntityManager.persistAndFlush(customer);

        // when
        Optional<Customer> expectedCustomer = customerRepository.findByUser(user);

        // then
        assertTrue(expectedCustomer.isPresent(), "Returned must not be null");
        assertEquals(customer.getFirstName(), expectedCustomer.get().getFirstName(), "First Name must be equal");

        testEntityManager.remove(customer);
        testEntityManager.flush();
    }

    @Test
    public void it_should_return_customer_of_that_email() {
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

        testEntityManager.persistAndFlush(customer);

        // when
        Optional<Customer> expectedCustomer = customerRepository.findByUser_Email(customer.getUser().getEmail());

        // then
        assertTrue(expectedCustomer.isPresent(), "Returned must not be null");
        assertEquals(customer.getUser().getEmail(), expectedCustomer.get().getUser().getEmail(), "Email must be equal");

        testEntityManager.remove(customer);
        testEntityManager.flush();
    }

    @Test
    public void it_should_return_customer_of_that_username() {
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

        testEntityManager.persistAndFlush(customer);

        // when
        Optional<Customer> expectedCustomer = customerRepository.findByUser_Username(customer.getUser().getUsername());

        // then
        assertTrue(expectedCustomer.isPresent(), "Returned must not be null");
        assertEquals(customer.getUser().getUsername(), expectedCustomer.get().getUser().getUsername());
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