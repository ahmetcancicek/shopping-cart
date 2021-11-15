package com.shopping.servivce;

import com.shopping.model.Product;
import com.shopping.repository.ProductRepository;
import com.shopping.servivce.impl.CartServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.util.CollectionUtils;

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

        assertFalse(CollectionUtils.isEmpty(products), "Cart must not be null");
        assertTrue(products.containsKey(product), "Cart must have the product");
        assertEquals(products.get(product), 1, "Cart have quantity of the product must equal to test case");
    }

    @Test
    void should_add_product_when_cart_is_not_empty() {
        Product product = Product.builder()
                .description("Product 1 - Description")
                .name("Product 1")
                .quantity(1)
                .price(new BigDecimal("10.00"))
                .build();

        cartService.addProduct(product);
        Map<Product, Integer> products = cartService.getProductsInCart();

        assertEquals(1, products.get(product), "Cart have quantity of the product must equal to test case");

        cartService.addProduct(product);
        assertEquals(2, products.get(product), "Cart have quantity of the product must equal to test case");
    }

    @Test
    void should_add_products_when_cart_is_empty() {
        Product product1 = Product.builder()
                .description("Product 1 - Description")
                .name("Product 1")
                .quantity(1)
                .price(new BigDecimal("10.00"))
                .build();

        Product product2 = Product.builder()
                .description("Product 2- Description")
                .name("Product 2")
                .quantity(2)
                .price(new BigDecimal("15.00"))
                .build();

        cartService.addProduct(product1);
        cartService.addProduct(product2);

        Map<Product, Integer> products = cartService.getProductsInCart();

        assertEquals(2, products.size(), "Cart have quantity of different products must equal to test case");
    }

    @Test
    void should_remove_product_when_cart_has_one_product() {
        Product product = Product.builder()
                .description("Product 1 - Description")
                .name("Product 1")
                .quantity(1)
                .price(new BigDecimal(10.00))
                .build();

        Map<Product, Integer> products = cartService.getProductsInCart();

        cartService.addProduct(product);
        cartService.removeProduct(product);
        assertNull(products.get(product), "Cart must be null");

        cartService.addProduct(product);
        cartService.addProduct(product);
        assertEquals(2, products.get(product), "Cart have quantity of the product must equal to test case");
    }


    @Test
    void should_remove_product_when_cart_has_more_products() {
        Product product1 = Product.builder()
                .description("Product 1 - Description")
                .name("Product 1")
                .quantity(1)
                .price(new BigDecimal("10.00"))
                .build();

        Product product2 = Product.builder()
                .description("Product 2- Description")
                .name("Product 2")
                .quantity(2)
                .price(new BigDecimal("15.00"))
                .build();

        Map<Product, Integer> products = cartService.getProductsInCart();

        cartService.addProduct(product1);
        cartService.addProduct(product2);
        assertEquals(2, products.size(), "Cart have quantity of different products must equal to test case");

        cartService.addProduct(product1);
        cartService.removeProduct(product2);
        assertEquals(1, products.size(), "Cart have quantity of different products must equal to test case");
        assertEquals(2, products.get(product1), "Cart have quantity of the product must equal to test case");

        cartService.removeProduct(product1);
        assertEquals(1, products.get(product1), "Cart have quantity of the product must equal to test case");
    }

    @Test
    void should_remove_product_when_cart_is_empty() {
        Product product = Product.builder()
                .description("Product 1 - Description")
                .name("Product 1")
                .quantity(1)
                .price(new BigDecimal(10.00))
                .build();

        Map<Product, Integer> products = cartService.getProductsInCart();

        cartService.removeProduct(product);
        assertTrue(CollectionUtils.isEmpty(products), "Cart must be null");

    }

    @Test
    void should_get_total_when_cart_is_empty() {
        assertEquals(new BigDecimal(0), cartService.getTotal(), "Total must be zero");
    }

    @Test
    void should_get_total_when_cart_has_one_product() {
        Product product = Product.builder()
                .description("Product 1 - Description")
                .name("Product 1")
                .quantity(1)
                .price(new BigDecimal("10.00"))
                .build();

        cartService.addProduct(product);
        assertTrue(cartService.getTotal().equals(product.getPrice()), "Total must be equal");

        cartService.addProduct(product);
        assertTrue(cartService.getTotal().equals(product.getPrice().multiply(BigDecimal.valueOf(2))), "Total must be equal");
    }

    @Test
    void should_get_total_when_cart_has_more_products() {
        Product product1 = Product.builder()
                .description("Product 1 - Description")
                .name("Product 1")
                .quantity(1)
                .price(new BigDecimal("10.50"))
                .build();

        Product product2 = Product.builder()
                .description("Product 2- Description")
                .name("Product 2")
                .quantity(2)
                .price(new BigDecimal("15.50"))
                .build();

        Map<Product, Integer> products = cartService.getProductsInCart();

        cartService.addProduct(product1);
        cartService.addProduct(product2);
        assertTrue(cartService.getTotal().equals(
                product1.getPrice().multiply(BigDecimal.valueOf(1))
                        .add(product2.getPrice().multiply(BigDecimal.valueOf(1)))
        ), "Total must be equal");

        cartService.addProduct(product1);
        cartService.removeProduct(product2);
        assertTrue(cartService.getTotal().equals(product1.getPrice().multiply(BigDecimal.valueOf(2))),
                "Total must be equal");

        cartService.removeProduct(product1);
        assertTrue(cartService.getTotal().equals(product1.getPrice().multiply(BigDecimal.valueOf(1))),
                "Total must be equal");
    }
}