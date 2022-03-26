package com.shopping.repository;

import com.shopping.model.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void should_save_product() {
        Product product = Product.builder()
                .name("Product 1")
                .description("Description")
                .price(BigDecimal.valueOf(10.0))
                .quantity(10)
                .build();

        Set<ConstraintViolation<Product>> violationSet = validator.validate(product);
        assertTrue(violationSet.isEmpty(), "Entity must be validate");

        Product savedProduct = productRepository.save(product);

        assertNotNull(savedProduct, "Returned must not be null");
        assertEquals(product.getName(), savedProduct.getName(), "Name must be equal");
    }

    @Test
    public void should_return_product_of_that_id_when_called_findById() {
        Product product = Product.builder()
                .name("Product 1")
                .description("Description")
                .price(BigDecimal.valueOf(10.0))
                .quantity(10)
                .build();

        productRepository.save(product);

        Optional<Product> fetchedProduct = productRepository.findById(product.getId());

        assertTrue(fetchedProduct.isPresent(), "Returned must not be null");
        assertEquals(product.getId(), fetchedProduct.get().getId(), "Id must be equal");
        assertEquals(product.getName(), fetchedProduct.get().getName(), "Name must be equal");
    }

    @Test
    public void should_delete_product_with_id() {
        Product product = Product.builder()
                .name("Product 1")
                .description("Description")
                .price(BigDecimal.valueOf(10.0))
                .quantity(10)
                .build();

        productRepository.save(product);
        productRepository.deleteById(product.getId());
    }


    @Test
    public void should_return_list_of_all_products() {
        Product product = Product.builder()
                .name("Product 1")
                .description("Description")
                .price(BigDecimal.valueOf(10.0))
                .quantity(10)
                .build();

        productRepository.save(product);

        List<Product> expectedProducts = productRepository.findAll();

        assertNotNull(expectedProducts, "List must not be null");
        assertEquals("Product 1", expectedProducts.get(0).getName(), "Name must be equal");
    }

}
