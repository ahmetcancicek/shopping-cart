package com.shopping.service.impl;

import com.shopping.model.Cart;
import com.shopping.model.CartItem;
import com.shopping.repository.CartItemRepository;
import com.shopping.service.CartItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;

    @Autowired
    public CartItemServiceImpl(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public void add(CartItem cartItem) {
        CartItem savedCartItem = cartItemRepository.saveAndFlush(cartItem);
        log.info("new cart item has been created: {}", savedCartItem.getId());
    }

    @Override
    public void removeById(Long id) {
        cartItemRepository.deleteById(id);
        log.info("cart item has been deleted: {}", id);
    }

    @Override
    public void removeAllCartItems(Cart cart) {
        cartItemRepository.deleteByCart(cart);
        log.info("all items of the cart has been deleted: {}", cart.getId());

    }
}
