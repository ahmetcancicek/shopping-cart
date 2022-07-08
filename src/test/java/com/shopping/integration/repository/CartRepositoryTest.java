package com.shopping.integration.repository;

import com.shopping.domain.model.*;
import com.shopping.repository.CartRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;


import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class CartRepositoryTest extends BaseRepositoryTest {
    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private CartRepository cartRepository;

    @Test
    void it_should_not_be_null() {
        assertThat(testEntityManager).isNotNull();
        assertThat(cartRepository).isNotNull();
    }

    private Product productOne;
    private Product productTwo;
    private Customer customer;
    private Cart cart;

    private CartItem item;

    @BeforeEach
    void setUp() {
        // Product
        productOne = Product.builder()
                .name("Galaxy S20")
                .price(BigDecimal.valueOf(750))
                .quantity(10)
                .serialNumber("XKA70BA")
                .build();

        testEntityManager.persistAndFlush(productOne);


        // Product
        productTwo = Product.builder()
                .name("Galaxy S10")
                .price(BigDecimal.valueOf(500))
                .quantity(5)
                .serialNumber("HA9N98N")
                .build();

        testEntityManager.persistAndFlush(productTwo);


        // Customer and customer cart
        customer = Customer.builder()
                .firstName("Christina")
                .lastName("King")
                .user(User.builder()
                        .email("christinaking@email.com")
                        .username("christinaking")
                        .password("XY80AXZ")
                        .active(true)
                        .build())
                .build();

        cart = Cart.builder()
                .totalPrice(BigDecimal.ZERO)
                .items(new HashSet<>())
                .customer(customer)
                .build();
        customer.setCart(cart);


        // Cart item
        item = CartItem.builder()
                .price(productOne.getPrice())
                .product(productOne)
                .quantity(1)
                .cart(cart)
                .build();
        cart.addItem(item);


        testEntityManager.persistAndFlush(customer);
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    public void it_should_return_cart_of_that_username_of_customer() {
        // when
        Optional<Cart> expectedCart = cartRepository.findByCustomer_User_Username("christinaking");

        //then
        assertTrue(expectedCart.isPresent(), "Returned must not be null");
        assertEquals("christinaking", expectedCart.get().getCustomer().getUser().getUsername(), "Customer must be equal");

    }

    @Test
    public void it_should_add_item_to_cart() {
        // given
        CartItem item = CartItem.builder()
                .price(productTwo.getPrice())
                .product(productTwo)
                .quantity(1)
                .cart(cart)
                .build();

        cart.addItem(item);

        // when
        Cart expectedCart = cartRepository.save(cart);

        // then
        assertNotNull(expectedCart, "Returned must not be null");
        assertEquals(2, cart.getItems().size(), "Items size must be equal");
    }

    @Test
    public void it_should_update_cart_items_from_cart() {
        // given
        item.setQuantity(10);
        cart.updateItem(item);

        // when
        Cart expectedCart = cartRepository.save(cart);

        // then
        assertNotNull(expectedCart, "Returned must not be null");
        assertEquals(1, expectedCart.getItems().size(), "Items size must equal");
        assertTrue(expectedCart.findItem(productOne).isPresent(), "");
        assertEquals(10, expectedCart.findItem(productOne).get().getQuantity(), "");

    }

    @Test
    public void it_should_clear() {
        // given
        cart.getItems().clear();

        // when
        Cart expectedCart = cartRepository.save(cart);

        // then
        assertNotNull(expectedCart, "Returned must not be null");
        assertEquals(0, expectedCart.getItems().size(), "Items size must equal");
    }


    @Test
    public void it_should_delete_item_from_cart() {
        // given
        cart.removeItem(item);

        // when
        Cart expectedCart = cartRepository.save(cart);

        // then
        assertNotNull(expectedCart, "Returned must not be null");
        assertEquals(0, expectedCart.getItems().size(), "Items size must equal one");
        assertFalse(expectedCart.findItem(productOne).isPresent(), "");
    }
}
