package com.shopping.service.impl;

import com.shopping.domain.exception.AlreadyExistsElementException;
import com.shopping.domain.exception.NoSuchElementFoundException;
import com.shopping.domain.model.User;
import com.shopping.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
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
    private BCryptPasswordEncoder passwordEncoder;

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
    public void it_should_delete_user() {
        // given
        User user = User.builder()
                .id(1L)
                .email("email@email.com")
                .username("username")
                .password("password")
                .active(true)
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // when
        userService.deleteById(1L);

        // then
        verify(userRepository, times(1)).deleteById(any());
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
        User expectedUser = userService.findByUsername(user.getUsername());

        // then
        verify(userRepository, times(1)).findByUsername(any());
        assertNotNull(expectedUser, "Returned must not be null");
        assertEquals(expectedUser.getUsername(), user.getUsername(), "Username must be equal");
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
        User expectedUser = userService.findByEmail(user.getEmail());

        // then
        verify(userRepository, times(1)).findByEmail(any());
        assertNotNull(expectedUser, "Returned must not be null");
        assertEquals(expectedUser.getEmail(), user.getEmail(), "Email must be equal");
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
        assertThat(throwable).isInstanceOf(AlreadyExistsElementException.class);
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
        assertThat(throwable).isInstanceOf(AlreadyExistsElementException.class);
    }


    @Test
    public void it_should_throw_exception_when_delete_user_that_does_not_exist() {
        // when
        Throwable throwable = catchThrowable(() -> {
            userService.deleteById(1L);
        });

        // then
        assertThat(throwable).isInstanceOf(NoSuchElementFoundException.class);
    }
}