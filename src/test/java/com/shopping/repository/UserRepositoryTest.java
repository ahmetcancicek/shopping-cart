package com.shopping.repository;

import com.shopping.domain.model.Role;
import com.shopping.domain.model.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;


class UserRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void it_should_save_user() {
        User user = User.builder()
                .username("username")
                .password("password")
                .email("email@email.com")
                .roles(new HashSet<>(Set.of(
                        Role.builder()
                                .id(1)
                                .name("USER")
                                .build()
                        ,
                        Role.builder()
                                .id(2)
                                .name("ADMIN")
                                .build()
                )))
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

        testEntityManager.remove(userOne);
        testEntityManager.flush();
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

        Throwable throwable = catchThrowable(() -> {
            userRepository.saveAndFlush(User.builder()
                    .username("username")
                    .password("passwordTwo")
                    .email("emailTwo@email.com")
                    .active(true)
                    .build());
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


}