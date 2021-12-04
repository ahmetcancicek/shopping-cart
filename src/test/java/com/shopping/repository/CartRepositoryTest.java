package com.shopping.repository;

import com.shopping.model.Cart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class CartRepositoryTest {

    @Autowired
    private CartRepository cartRepository;
    private Cart cart;

    @BeforeEach
    void setUp() {
        cart = Cart.builder().build();
    }

    @Test
    public void should_save_cart() {
        cartRepository.save(cart);

        Optional<Cart> fetched = cartRepository.findById(cart.getId());

        assertTrue(fetched.isPresent());

        assertEquals(cart.getId(), fetched.get().getId(), "Id must be equal");
    }

    @Test
    public void should_return_of_that_cart_when_called_findById() {
        cartRepository.save(cart);

        Optional<Cart> fetchedCart = cartRepository.findById(cart.getId());

        assertTrue(fetchedCart.isPresent());

        assertEquals(cart.getId(), fetchedCart.get().getId());
    }
}
