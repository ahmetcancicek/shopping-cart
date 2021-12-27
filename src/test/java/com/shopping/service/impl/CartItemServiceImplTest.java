package com.shopping.service.impl;

import com.shopping.model.Cart;
import com.shopping.model.CartItem;
import com.shopping.model.Product;
import com.shopping.repository.CartItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartItemServiceImplTest {

    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private CartItemServiceImpl cartItemService;


    @Test
    void should_add_cart_item() {
        final CartItem item = CartItem.builder()
                .id(1L)
                .price(BigDecimal.valueOf(10.00))
                .quantity(10)
                .build();

        when(cartItemRepository.saveAndFlush(any(CartItem.class))).thenReturn(item);
        cartItemService.add(item);

        verify(cartItemRepository, times(1)).saveAndFlush(any());
    }

    @Test
    void should_remove_cart_item() {
        final Long cartItemId = 1L;

        cartItemService.removeById(cartItemId);
        cartItemService.removeById(cartItemId);

        verify(cartItemRepository, times(2)).deleteById(any());
    }

    @Test
    void should_remove_all_cart_items() {
        final Cart cart = Cart.builder()
                .id(1L)
                .totalPrice(new BigDecimal("10.0"))
                .build();

        cartItemService.removeAllCartItems(cart);

        verify(cartItemRepository, times(1)).deleteByCart(any());
    }

}
