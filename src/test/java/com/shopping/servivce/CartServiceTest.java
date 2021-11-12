package com.shopping.servivce;

import com.shopping.model.Product;
import com.shopping.repository.ProductRepository;
import com.shopping.servivce.impl.CartServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CartServiceTest {

    ProductRepository productRepository;
    CartService cartService;

    @BeforeEach
    void setUp() {
        Mockito.mock(ProductRepository.class);
        cartService = new CartServiceImpl(productRepository);
    }

    @Test
    void should_add_product_when_cart_is_empty() {
        Product product = Product.builder()
                .description("Product 1 - Description")
                .name("Product 1")
                .quantity(1)
                .price(new BigDecimal(10.00))
                .build();

        cartService.addProduct(product);
        Map<Product, Integer> products = cartService.getProductsInCart();

        assertNotNull(products);
        assertTrue(products.containsKey(product));
        assertEquals(products.get(product),1);
    }

    @Test
    void should_add_product_when_cart_is_not_empty(){
        Product product = Product.builder()
                .description("Product 1 - Description")
                .name("Product 1")
                .quantity(1)
                .price(new BigDecimal(10.00))
                .build();

        cartService.addProduct(product);
        Map<Product, Integer> products = cartService.getProductsInCart();

        assertEquals(products.get(product), 1);

        cartService.addProduct(product);
        assertEquals(products.get(product), 2);
    }

    @Test
    void should_remove_product_when_cart_has_one_product(){

    }

    @Test
    void should_remove_product_when_cart_has_more_products(){

    }

    @Test
    void should_remove_product_when_cart_is_empty(){

    }

    @Test
    void should_get_total_when_cart_is_empty(){

    }

    @Test
    void should_get_total_when_cart_has_any_product(){

    }
}