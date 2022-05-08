package com.shopping.repository;

import com.shopping.model.Cart;
import com.shopping.model.CartItem;
import com.shopping.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(value = {"/import.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/clear.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class CartRepositoryTest {
    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private CartRepository cartRepository;

    @Container
    public static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql");

    @Test
    public void it_should_db_run() {
        assertTrue(mysql.isRunning(), "MySQL in not running");
    }

    @Test
    public void it_should_return_cart_of_that_username_of_customer() {
        // when
        Optional<Cart> cart = cartRepository.findByCustomer_User_Username("lucycar");

        //then
        assertTrue(cart.isPresent(), "Returned must not be null");
        assertEquals(1000L, cart.get().getId(), "Id must be equal");
    }

    @Test
    public void it_should_add_item_when_cart_is_empty_that_of_customer() {
        // given
        Cart cart = testEntityManager.find(Cart.class, 1000L);
        Product product = testEntityManager.find(Product.class, 3000L);

        CartItem item = CartItem.builder()
                .cart(cart)
                .quantity(1)
                .price(product.getPrice())
                .build();

        cart.addItem(item);

        // when
        Cart savedCart = cartRepository.save(cart);

        // then
        assertNotNull(savedCart);
        assertEquals(item.getPrice(), savedCart.getItems().stream().filter(cartItem -> {
            return cartItem.getCart().equals(cart);
        }).findFirst().get().getPrice());
    }

    @Test
    public void it_should_add_item_when_cart_is_not_empty_that_of_customer() {
        // given
        Cart cart = testEntityManager.find(Cart.class, 2000L);
        Product product = testEntityManager.find(Product.class, 3000L);

        CartItem item = CartItem.builder()
                .cart(cart)
                .quantity(1)
                .price(product.getPrice())
                .build();

        cart.addItem(item);

        // when
        Cart savedCart = cartRepository.save(cart);

        // then
        assertNotNull(savedCart);
        assertEquals(item.getPrice(), savedCart.getItems().stream().filter(cartItem -> {
            return cartItem.getCart().equals(cart);
        }).findFirst().get().getPrice());
        assertEquals(2, savedCart.getItems().size());
    }

    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.jpa.hibernate.dll-auto", () -> "create-drop");
        registry.add("spring.jpa.properties.hibernate.dialect", () -> "org.hibernate.dialect.MySQL8Dialect");
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }
}
