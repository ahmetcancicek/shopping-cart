package com.shopping.service.impl;

import com.shopping.exception.UserAlreadyExistsException;
import com.shopping.exception.UserNotFoundException;
import com.shopping.model.User;
import com.shopping.repository.UserRepository;
import com.shopping.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> {
            log.info("user does not exist with username: {}", username);
            return new UserNotFoundException(String.format("user does not exist with username: {%s}", username));
        });
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> {
            log.info("user does not exist with email: {}", email);
            return new UserNotFoundException(String.format("user does not exist with email: {%s}", email));
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
            throw new UserAlreadyExistsException("user already exists with email: {" + it.getEmail() + "}");
        });

        userRepository.findByUsername(user.getUsername()).ifPresent((it) -> {
            log.error("user already exists with username: {}" + it.getUsername());
            throw new UserAlreadyExistsException("user already exists with username: {" + it.getUsername() + "}");
        });
    }

    @Override
    public void deleteById(Long id) {
        userRepository.findById(id).orElseThrow(() -> {
            log.info("user does not exist with id: {}", id);
            return new UserNotFoundException(String.format("user does not exist with id: {%d}", id));
        });
        userRepository.deleteById(id);
        log.info("user has been deleted with ID: {}", id);
    }
}
