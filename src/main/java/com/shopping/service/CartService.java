package com.shopping.service;

import com.shopping.domain.dto.CartItemRequest;
import com.shopping.domain.dto.CartResponse;

public interface CartService {

    CartResponse findByUsername(String username);

    CartResponse addItemToCart(String username, String serialNumber);

    CartResponse addItemToCart(String username, String serialNumber, int quantity);

    CartResponse addItemToCart(String username, CartItemRequest cartItemRequest);

    CartResponse deleteItemFromCart(String username, String serialNumber);

    CartResponse clear(String username);

    CartResponse updateItemFromCart(String username, CartItemRequest cartItemRequest);

    CartResponse updateItemFromCart(String username, String serialNumber, int quantity);
}
