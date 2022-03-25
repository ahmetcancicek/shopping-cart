package com.shopping.service.impl;

import com.shopping.model.User;
import com.shopping.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void should_return_user_of_that_username_when_called_findByUsername() {
        User user = User.builder()
                .id(1L)
                .username("username")
                .password("password")
                .email("email@email.com")
                .active(true)
                .build();

        given(userRepository.findByUsername(user.getUsername())).willReturn(Optional.of(user));

        Optional<User> expected = userService.findByUsername(user.getUsername());

        verify(userRepository, times(1)).findByUsername(any());
        assertTrue(expected.isPresent(), "Returned must not be null");
        assertEquals(user.getUsername(), expected.get().getUsername(), "Username must be equal");
    }

    @Test
    public void should_return_user_of_that_email_when_called_findByEmail() {
        User user = User.builder()
                .id(1L)
                .username("username")
                .password("password")
                .email("email@email.com")
                .active(true)
                .build();

        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));

        Optional<User> expected = userService.findByEmail(user.getEmail());

        verify(userRepository, times(1)).findByEmail(any());
        assertTrue(expected.isPresent(), "Returned must not be null");
        assertEquals(user.getEmail(), expected.get().getEmail(), "Email must be equal");
    }


    @Test
    public void should_save_user() {
        User user = User.builder()
                .id(1L)
                .username("username")
                .password("password")
                .email("email@email.com")
                .active(true)
                .build();

        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userService.save(user);

        verify(userRepository, times(1)).save(any());
        assertNotNull(savedUser, "Returned must not be null");
        assertEquals(user.getEmail(), savedUser.getEmail(), "Email must be equal");

    }

    @Test
    public void should_throw_exception_when_save_user_with_existing_email() {
        User user = User.builder()
                .id(1L)
                .username("username")
                .password("password")
                .email("email@email.com")
                .active(true)
                .build();

        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class, () -> {
            userService.save(user);
        });

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void should_throw_exception_when_save_user_with_existing_username() {
        User user = User.builder()
                .id(1L)
                .username("username")
                .password("password")
                .email("email@email.com")
                .active(true)
                .build();

        given(userRepository.findByUsername(user.getUsername())).willReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class, () -> {
            userService.save(user);
        });

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void should_delete_user_when_called_deleteById() {
        final Long userId = 1L;

        userService.deleteById(userId);
        userService.deleteById(userId);

        verify(userRepository, times(2)).deleteById(userId);
    }
}