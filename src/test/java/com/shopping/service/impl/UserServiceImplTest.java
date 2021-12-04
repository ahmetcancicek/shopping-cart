package com.shopping.service.impl;

import com.shopping.model.User;
import com.shopping.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

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
    public void it_should_return_user_of_that_username_when_called_findByUsername() {
        final User user = User.builder()
                .username("username")
                .password("password")
                .email("email@email.com")
                .active(true)
                .build();

        given(userRepository.findByUsername(user.getUsername())).willReturn(Optional.of(user));

        final Optional<User> expected = userService.findByUsername(user.getUsername());

        assertTrue(expected.isPresent());

        assertEquals(user.getUsername(), expected.get().getUsername(), "Username must be equal");
    }

    @Test
    public void it_should_return_user_of_that_email_when_called_findByEmail() {
        final User user = User.builder()
                .username("username")
                .password("password")
                .email("email@email.com")
                .active(true)
                .build();

        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));

        final Optional<User> expected = userService.findByEmail(user.getEmail());

        assertTrue(expected.isPresent());

        assertEquals(user.getEmail(), expected.get().getEmail(),"Email must be equal");
    }


    @Test
    public void it_should_save_user() {
        final User user = User.builder()
                .username("username")
                .password("password")
                .email("email@email.com")
                .active(true)
                .build();


        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userService.save(user);

        verify(userRepository, times(1)).save(any());

        assertNotNull(savedUser);

    }

    @Test
    public void it_should_throw_exception_when_save_user_with_existing_email() {
        final User user = User.builder()
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
    public void it_should_throw_exception_when_save_user_with_existing_username() {
        final User user = User.builder()
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
    public void it_should_delete_user_when_delete_user_with_id() {

        final Long userId = 1L;

        userService.deleteById(userId);
        userService.deleteById(userId);

        verify(userRepository, times(2)).deleteById(userId);
    }
}