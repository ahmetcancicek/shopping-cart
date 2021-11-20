package com.shopping.servivce.impl;

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
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    public void findByUsername_ExistingUsername_ShouldReturnUserOfThatUsername() {
        final User user = User.builder()
                .username("username")
                .password("password")
                .email("email@email.com")
                .active(true)
                .build();

        given(userRepository.findByUsername(user.getUsername())).willReturn(Optional.of(user));

        final Optional<User> expected = userService.findByUsername(user.getUsername());

        assertTrue(expected.isPresent());

        assertEquals(user.getUsername(), expected.get().getUsername());
    }

    @Test
    public void findByEmail_ExistingEmail_ShouldReturnUserOfThatEmail() {
        final User user = User.builder()
                .username("username")
                .password("password")
                .email("email@email.com")
                .active(true)
                .build();

        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));

        final Optional<User> expected = userService.findByEmail(user.getEmail());

        assertTrue(expected.isPresent());

        assertEquals(user.getEmail(), expected.get().getEmail());
    }

    @Test
    public void saveUser_ValidUser_ShouldSaveNewUser() {
        final User user = User.builder()
                .username("username")
                .password("password")
                .email("email@email.com")
                .active(true)
                .build();


        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userService.saveUser(user);

        verify(userRepository, times(1)).save(any());

        assertNotNull(savedUser);

    }

    @Test
    public void saveUser_ExistingUserWithEmail_ShouldThrowException() {
        final User user = User.builder()
                .username("username")
                .password("password")
                .email("email@email.com")
                .active(true)
                .build();

        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class, () -> {
            userService.saveUser(user);
        });

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void saveUser_ExistingUserWithUsername_ShouldThrowException() {
        final User user = User.builder()
                .username("username")
                .password("password")
                .email("email@email.com")
                .active(true)
                .build();

        given(userRepository.findByUsername(user.getUsername())).willReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class,()->{
            userService.saveUser(user);
        });

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void deleteUserById_ExistingUserWithId_ShouldDeleteUser() {

        final Long userId = 1L;

        userService.deleteUserById(userId);
        userService.deleteUserById(userId);

        verify(userRepository, times(2)).deleteById(userId);
    }
}