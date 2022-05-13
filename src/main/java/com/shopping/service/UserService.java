package com.shopping.service;

import com.shopping.domain.model.User;

public interface UserService {
    User findByUsername(String username);
    User findByEmail(String email);
    User save(User user);
    void deleteById(Long id);
}
