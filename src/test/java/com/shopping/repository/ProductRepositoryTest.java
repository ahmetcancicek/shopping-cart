package com.shopping.repository;

import com.shopping.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;
    private Product product;


    @BeforeEach
    void setUp() {
        product = new Product("Product 1", "Description", BigDecimal.valueOf(10.00), 10);
    }

    @Test
    public void should_save_product() {
        Product savedProduct = productRepository.saveAndFlush(product);

        assertNotNull(savedProduct, "Returned must not be null");
        assertEquals(product.getName(), savedProduct.getName(), "Name must be equal");
    }

    @Test
    public void should_return_product_of_that_id_when_called_findById() {
        productRepository.saveAndFlush(product);

        Optional<Product> fetchedProduct = productRepository.findById(product.getId());

        assertTrue(fetchedProduct.isPresent(), "Returned must not be null");
        assertEquals(product.getName(), fetchedProduct.get().getName(), "Name must be equal");
    }

    @Test
    public void should_delete_product_with_id() {
        productRepository.saveAndFlush(product);
        productRepository.deleteById(product.getId());
    }


    @Test
    public void should_return_list_of_all_products() {
        productRepository.saveAndFlush(product);
        List<Product> expectedProducts = productRepository.findAll();

        assertEquals("Product 1", expectedProducts.get(0).getName(), "Name must be equal");

    }

}
