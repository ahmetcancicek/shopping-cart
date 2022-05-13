package com.shopping.repository;


import com.shopping.domain.model.User;
import org.junit.jupiter.api.*;
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
class UserRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private UserRepository userRepository;

    @Container
    public static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql");

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

        User createdUser = userRepository.save(user);
        User expectedUser = testEntityManager.find(User.class, createdUser.getId());

        assertEquals("username", expectedUser.getUsername(), "Username must be equal");

        testEntityManager.remove(user);
        testEntityManager.flush();
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

        assertNull(testEntityManager.find(User.class, userId), "Returned must be null");
    }

    @Test
    public void it_should_return_user_of_that_id() {
        User user = User.builder()
                .username("username")
                .password("password")
                .email("email@email.com")
                .active(true)
                .build();

        Object id = testEntityManager.persistAndGetId(user);

        Optional<User> expectedUser = userRepository.findById((Long) id);

        assertTrue(expectedUser.isPresent(), "Returned must not be null");
        assertEquals("username", expectedUser.get().getUsername(), "Username must be equal");
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

        Optional<User> expectedUser = userRepository.findByUsername("username");

        assertTrue(expectedUser.isPresent(), "Returned must not be null");
        assertEquals("username", expectedUser.get().getUsername(), "Username must be equal");

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

        Optional<User> expectedUser = userRepository.findByEmail("email@email.com");

        assertTrue(expectedUser.isPresent(), "Returned must not be null");
        assertEquals("email@email.com", expectedUser.get().getEmail(), "Email must be equal");

        testEntityManager.remove(user);
        testEntityManager.flush();

    }

    @Test
    public void it_should_throw_exception_when_save_user_with_existing_email() {
        User userOne = User.builder()
                .username("usernameOne")
                .password("passwordOne")
                .email("email@email.com")
                .active(true)
                .build();
        testEntityManager.persistAndFlush(userOne);

        User userTwo = User.builder()
                .username("usernameTwo")
                .password("passwordTwo")
                .email("email@email.com")
                .active(true)
                .build();

        Throwable throwable = catchThrowable(() -> {
            userRepository.saveAndFlush(userTwo);
        });

        assertThat(throwable).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    public void it_should_throw_exception_when_save_user_with_existing_username() {
        User userOne = User.builder()
                .username("username")
                .password("passwordOne")
                .email("emailOne@email.com")
                .active(true)
                .build();
        testEntityManager.persistAndFlush(userOne);

        User userTwo = User.builder()
                .username("username")
                .password("passwordTwo")
                .email("emailTwo@email.com")
                .active(true)
                .build();

        Throwable throwable = catchThrowable(() -> {
            userRepository.saveAndFlush(userTwo);
        });

        assertThat(throwable).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    public void it_should_throw_exception_when_user_that_does_not_exist() {
        Throwable throwable = catchThrowable(() -> {
            userRepository.deleteById(12823L);
        });

        assertThat(throwable).isInstanceOf(Exception.class);
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