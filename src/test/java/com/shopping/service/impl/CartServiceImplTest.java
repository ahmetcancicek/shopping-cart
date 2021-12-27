package com.shopping.service.impl;

import com.shopping.model.Cart;
import com.shopping.repository.CartRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    @Test
    public void should_return_cart_of_that_id_when_called_findById() {
        final Cart cart = Cart.builder()
                .id(1L)
                .totalPrice(BigDecimal.valueOf(0.00))
                .build();

        given(cartRepository.findById(cart.getId())).willReturn(Optional.of(cart));

        final Optional<Cart> expected = cartService.findById(cart.getId());

        assertTrue(expected.isPresent(), "Returned must not be null");
        assertEquals(cart.getId(), expected.get().getId(), "Id must be equal");
    }
}
