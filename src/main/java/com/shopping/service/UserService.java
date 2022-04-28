package com.shopping.service;

import com.shopping.model.User;

import java.util.Optional;

public interface UserService {
    User findByUsername(String username);
    User findByEmail(String email);
    User save(User user);
    void deleteById(Long id);
}
