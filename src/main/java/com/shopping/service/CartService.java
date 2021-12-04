package com.shopping.service;

import com.shopping.model.Cart;

import java.util.Optional;

public interface CartService {
    Optional<Cart> findById(Long id);
}
