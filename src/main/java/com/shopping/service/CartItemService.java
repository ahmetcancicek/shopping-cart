package com.shopping.service;

import com.shopping.model.Cart;
import com.shopping.model.CartItem;

public interface CartItemService {
    void add(CartItem cartItem);
    void removeById(Long id);
    void removeAllCartItems(Cart cart);
}
