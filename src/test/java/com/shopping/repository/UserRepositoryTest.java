package com.shopping.repository;


import com.shopping.model.User;
import org.junit.After;
import org.junit.jupiter.api.*;
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

import static org.junit.jupiter.api.Assertions.*;


@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private UserRepository userRepository;

    @Container
    public static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql");

    @After
    public void clear() {
        this.testEntityManager.clear();
    }

    @Test
    public void it_should_db_run() {
        assertTrue(mysql.isRunning(), "MySQL in not running");
    }

    @Test
    public void it_should_save_user() {
        User user = User.builder()
                .username("username")
                .password("password")
                .email("email@email.com")
                .active(true)
                .build();

        Object userId = testEntityManager.persistAndGetId(user);
        testEntityManager.flush();

        Optional<User> createdUser = userRepository.findById((Long) userId);

        assertTrue(createdUser.isPresent(), "Returned must not be null");
        assertEquals("username", createdUser.get().getUsername(), "Username must be equal");

        testEntityManager.remove(user);
        testEntityManager.flush();
    }

    @Test
    public void it_should_return_user_of_that_username() {
        User user = User.builder()
                .username("username")
                .password("password")
                .email("email@email.com")
                .active(true)
                .build();

        testEntityManager.persistAndFlush(user);

        Optional<User> createdUser = userRepository.findByUsername("username");

        assertTrue(createdUser.isPresent(), "Returned must not be null");
        assertEquals("username", createdUser.get().getUsername(), "Username must be equal");

        testEntityManager.remove(user);
        testEntityManager.flush();

    }

    @Test
    public void it_should_return_user_of_that_email() {
        User user = User.builder()
                .username("username")
                .password("password")
                .email("email@email.com")
                .active(true)
                .build();

        testEntityManager.persistAndFlush(user);

        Optional<User> createdUser = userRepository.findByEmail("email@email.com");

        assertTrue(createdUser.isPresent(), "Returned must not be null");
        assertEquals("email@email.com", createdUser.get().getEmail(), "Email must be equal");

        testEntityManager.remove(user);
        testEntityManager.flush();

    }

    @Test
    public void it_should_throw_exception_when_save_user_with_existing_email() {
        // TODO:
    }

    @Test
    public void it_should_throw_exception_when_save_user_with_existing_username() {
        // TODO:
    }

    @Test
    public void it_should_delete_user() {
        User user = User.builder()
                .username("username")
                .password("password")
                .email("email@email.com")
                .active(true)
                .build();

        Object userId = testEntityManager.persistAndGetId(user);
        userRepository.deleteById((Long) userId);
        testEntityManager.flush();

        Optional<User> deleteUser = userRepository.findById((Long) userId);

        assertFalse(deleteUser.isPresent(), "Returned must be null");
    }

    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.jpa.hibernate.dll-auto", () -> "create-drop");
        registry.add("spring.jpa-database-platform", () -> "org.hibernate.dialect.MySQL5InnoDBDialect");
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }
}