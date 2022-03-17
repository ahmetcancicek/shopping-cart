package com.shopping.service;

import com.shopping.model.Cart;
import com.shopping.model.Customer;
import com.shopping.model.Product;

import java.util.Optional;

public interface CartService {
    Optional<Cart> findById(Long id);

    Optional<Cart> findByUsername(String username);

    Optional<Cart> findByCustomerId(Long id);

    Optional<Cart> findByCustomer(Customer customer);

    void addItemToCart(Customer customer, Product product);

    void addItemToCart(Customer customer, Long productId);

    void addItemToCart(Customer customer, Product product, int quantity);

    void addItemToCart(Long customerId, Long productId);

    void removeAllCartItems(Customer customer);

    void removeAllCartItems(Cart cart);

    void removeItem(Customer customer, Product product);
}
