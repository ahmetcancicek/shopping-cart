package com.shopping.service.impl;

import com.shopping.exception.AlreadyExistsElementException;
import com.shopping.exception.NoSuchElementFoundException;
import com.shopping.model.User;
import com.shopping.repository.UserRepository;
import com.shopping.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Service(value = "userService")
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> {
            log.error("user does not exist with username: {}", username);
            return new NoSuchElementFoundException(String.format("user does not exist with username: %s", username));
        });
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> {
            log.error("user does not exist with email: {}", email);
            return new NoSuchElementFoundException(String.format("user does not exist with email: %s", email));
        });
    }

    @Override
    public User save(User user) {
        isExisted(user);
        //
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(true);
        User savedUser = userRepository.save(user);
        //
        log.info("new user has been created with ID: {}", user.getId());
        return savedUser;
    }

    private void isExisted(User user) {
        userRepository.findByEmail(user.getEmail()).ifPresent((it) -> {
            log.error("user already exists with email: {}", it.getEmail());
            throw new AlreadyExistsElementException("user already exists with email: {" + it.getEmail() + "}");
        });

        userRepository.findByUsername(user.getUsername()).ifPresent((it) -> {
            log.error("user already exists with username: {}" + it.getUsername());
            throw new AlreadyExistsElementException("user already exists with username: {" + it.getUsername() + "}");
        });
    }

    @Override
    public void deleteById(Long id) {
        userRepository.findById(id).orElseThrow(() -> {
            log.error("user does not exist with id: {}", id);
            return new NoSuchElementFoundException(String.format("user does not exist with id: {%d}", id));
        });
        userRepository.deleteById(id);
        log.info("user has been deleted with ID: {}", id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> byUsername = userRepository.findByUsername(username);
        return byUsername.orElseThrow(() -> new UsernameNotFoundException("Username does not exist"));
    }
}
