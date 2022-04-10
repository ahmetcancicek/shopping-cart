package com.shopping.service.impl;

import com.shopping.exception.UserAlreadyExistsException;
import com.shopping.model.User;
import com.shopping.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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

    @Test
    public void it_should_save_user() {
        // given
        User user = User.builder()
                .id(1L)
                .username("username")
                .password("password")
                .email("email@email.com")
                .active(true)
                .build();
        when(userRepository.save(any(User.class))).thenReturn(user);

        // when
        User savedUser = userService.save(user);

        // then
        verify(userRepository, times(1)).save(any());
        assertNotNull(savedUser, "Returned must not be null");
        assertEquals(savedUser.getEmail(), user.getEmail(), "Email must be equal");

    }

    @Test
    public void it_should_return_user_of_that_username() {
        // given
        User user = User.builder()
                .id(1L)
                .username("username")
                .password("password")
                .email("email@email.com")
                .active(true)
                .build();
        given(userRepository.findByUsername(user.getUsername())).willReturn(Optional.of(user));

        // when
        Optional<User> expected = userService.findByUsername(user.getUsername());

        // then
        verify(userRepository, times(1)).findByUsername(any());
        assertTrue(expected.isPresent(), "Returned must not be null");
        assertEquals(expected.get().getUsername(), user.getUsername(), "Username must be equal");
    }

    @Test
    public void it_should_return_user_of_that_email() {
        // given
        User user = User.builder()
                .id(1L)
                .username("username")
                .password("password")
                .email("email@email.com")
                .active(true)
                .build();
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        // when
        Optional<User> expected = userService.findByEmail(user.getEmail());

        // then
        verify(userRepository, times(1)).findByEmail(any());
        assertTrue(expected.isPresent(), "Returned must not be null");
        assertEquals(expected.get().getEmail(), user.getEmail(), "Email must be equal");
    }

    @Test
    public void it_should_throw_exception_when_save_user_with_existing_email() {
        // given
        User user = User.builder()
                .id(1L)
                .username("username")
                .password("password")
                .email("email@email.com")
                .active(true)
                .build();
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        // when
        Throwable throwable = catchThrowable(() -> {
            userService.save(user);
        });

        // then
        assertThat(throwable).isInstanceOf(UserAlreadyExistsException.class);
    }

    @Test
    public void it_should_throw_exception_when_save_user_with_existing_username() {
        // given
        User user = User.builder()
                .id(1L)
                .username("username")
                .password("password")
                .email("email@email.com")
                .active(true)
                .build();
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        // when
        Throwable throwable = catchThrowable(() -> {
            userService.save(user);
        });

        // then
        assertThat(throwable).isInstanceOf(UserAlreadyExistsException.class);
    }

    @Test
    public void it_should_delete_user() {
        // given
        Long userId = 1L;

        // when
        userService.deleteById(userId);
        userService.deleteById(userId);

        // then
        verify(userRepository, times(2)).deleteById(userId);
    }
}