package com.shopping.service;

import com.shopping.dto.CartResponse;

public interface CartService {

    CartResponse findByUsername(String username);

    CartResponse addItemToCart(String username, String serialNumber);

    CartResponse addItemToCart(String username, String serialNumber, int quantity);

}
