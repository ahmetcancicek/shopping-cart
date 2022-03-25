package com.shopping.service.impl;

import com.shopping.model.User;
import com.shopping.repository.UserRepository;
import com.shopping.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("userService")
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
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User save(User user) {
        isExist(user);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(true);
        User savedUser = userRepository.save(user);
        log.info("new user has been created: {}", user.getId());

        return savedUser;
    }

    private void isExist(User user) {
        Optional<User> existing = userRepository.findByEmail(user.getEmail());
        existing.ifPresent(it -> {
            log.error("user already exists: " + it.getEmail());
            throw new IllegalArgumentException("user already exists: {}" + it.getEmail());
        });

        existing = userRepository.findByUsername(user.getUsername());
        existing.ifPresent(it -> {
            log.error("user already exists: " + it.getUsername());
            throw new IllegalArgumentException("user already exists: {}" + it.getUsername());
        });
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
        log.info("user has been deleted: {}", id);
    }
}
