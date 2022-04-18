package com.shopping.repository;

import com.shopping.model.Customer;
import com.shopping.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

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

        Object customerId = testEntityManager.persistAndGetId(customer);
        testEntityManager.flush();

        Optional<Customer> createdCustomer = customerRepository.findById((Long) customerId);

        assertTrue(createdCustomer.isPresent(), "Returned must not be null");

        testEntityManager.remove(user);
        testEntityManager.flush();
    }

    @Test
    public void it_should_save_customer_with_payment_and_address() {

    }

    @Test
    public void it_should_throw_exception_when_save_customer_with_existing_email() {

    }

    @Test
    public void it_should_throw_exception_when_save_customer_with_existing_username() {

    }

    @Test
    public void it_should_return_cart_id_when_save_customer() {

    }

    @Test
    public void it_should_return_list_of_all_customers() {

    }

    @Test
    public void it_should_return_customer_of_that_id() {

    }

    @Test
    public void it_should_return_customer_of_that_user() {

    }

    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.jpa.hibernate.dll-auto", () -> "create-drop");
        registry.add("spring.jpa-database-platform", () -> "org.hibernate.dialect.MySQL5InnoDBDDialect");
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }
}