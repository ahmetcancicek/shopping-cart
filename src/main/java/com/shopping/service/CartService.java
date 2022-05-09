package com.shopping.service;

import com.shopping.dto.CartItemRequest;
import com.shopping.dto.CartResponse;

public interface CartService {

    CartResponse findByUsername(String username);

    CartResponse addItemToCart(String username, String serialNumber);

    CartResponse addItemToCart(String username, String serialNumber, int quantity);

    CartResponse addItemToCart(String username, CartItemRequest cartItemRequest);

    CartResponse addItemToCart(CartItemRequest cartItemRequest);

    CartResponse deleteItemFromCart(String username, String serialNumber);

    CartResponse clear(String username);

    CartResponse updateItemFromCart(CartItemRequest cartItemRequest);

    CartResponse updateItemFromCart(String username, String serialNumber, int quantity);
}
