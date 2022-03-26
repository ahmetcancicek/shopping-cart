package com.shopping.repository;

import com.shopping.model.Customer;
import com.shopping.model.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void should_save_user() {
        User user = User.builder()
                .email("email@email.com")
                .username("username")
                .password("password")
                .active(true)
                .build();

        Set<ConstraintViolation<User>> violationSet = validator.validate(user);
        assertTrue(violationSet.isEmpty(), "Entity must be validate");

        User savedUser = userRepository.save(user);

        assertNotNull(savedUser, "Returned must not be null");
        assertNotNull(savedUser.getId(), "Id must not be null");
        assertEquals(user.getUsername(), savedUser.getUsername(), "Username must be equal");
    }

    @Test
    public void should_return_user_of_that_username_when_called_findByUsername() {
        User user = User.builder()
                .email("email@email.com")
                .username("username")
                .password("password")
                .active(true)
                .build();

        userRepository.save(user);

        Optional<User> fetchedUser = userRepository.findByUsername(user.getUsername());

        assertTrue(fetchedUser.isPresent(), "Returned must not be null");
        assertEquals(user.getId(), fetchedUser.get().getId(), "Id must be equal");
        assertEquals(user.getUsername(), fetchedUser.get().getUsername(), "Username must be equal");
    }

    @Test
    public void should_return_user_of_that_email_when_called_findByEmail() {
        User user = User.builder()
                .email("email@email.com")
                .username("username")
                .password("password")
                .active(true)
                .build();

        userRepository.save(user);

        Optional<User> fetchedUser = userRepository.findByEmail(user.getEmail());

        assertTrue(fetchedUser.isPresent(), "Returned must not be null");
        assertEquals(user.getId(), fetchedUser.get().getId(), "Id must be equal");
        assertEquals(user.getEmail(), fetchedUser.get().getEmail(), "Email must be equal");
    }

    @Test
    public void should_throw_exception_when_save_user_with_existing_email() {

    }

    @Test
    public void should_throw_exception_when_save_user_with_existing_username() {

    }
}