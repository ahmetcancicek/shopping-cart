package com.shopping.repository;

import com.shopping.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class CartItemRepositoryTest {

    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ProductRepository productRepository;

    private Customer customer;
    private Cart cart;
    private CartItem cartItem;
    private Product product;


    @BeforeEach
    void setUp() {
        // Customer - Cart
        customer = new Customer("John", "Doe", new User("email@email.com", "username", "password", true));
        cart = new Cart(customer, BigDecimal.valueOf(10.00));
        customer.setCart(cart);
        Customer savedCustomer = customerRepository.saveAndFlush(customer);

        // Product
        product = new Product("Product 1", "Description", BigDecimal.valueOf(10.00), 5);
        productRepository.saveAndFlush(product);

        // Item
        cartItem = new CartItem(product, 10, BigDecimal.valueOf(10.00), cart);


    }

    @AfterEach
    void tearDown() {
        customerRepository.delete(customer);
        productRepository.delete(product);
    }

    @Test
    public void should_add_item_to_cart() {
        CartItem savedItem = cartItemRepository.saveAndFlush(cartItem);

        assertNotNull(savedItem, "Returned must not be null");
        assertEquals(savedItem.getQuantity(), savedItem.getQuantity(), "Number of quantity must be equal");
    }

    @Test
    public void should_remove_item_from_cart() {
        cartItemRepository.saveAndFlush(cartItem);
        cartItemRepository.deleteById(cartItem.getId());
    }

    @Test
    public void should_remove_all_items_from_cart() {
        cartItemRepository.saveAndFlush(cartItem);

        List<CartItem> cartItems = cartItemRepository.findAll();
        for (CartItem cartItem : cartItems) {
            cartItemRepository.deleteById(cartItem.getId());
        }
    }
}
