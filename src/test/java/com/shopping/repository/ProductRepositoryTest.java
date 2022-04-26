package com.shopping.repository;

import com.shopping.model.Customer;
import com.shopping.model.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private ProductRepository productRepository;

    @Container
    public static MySQLContainer<?> mysql = new MySQLContainer<>("mysql");

    public static Stream<Arguments> product_request() {
        return Stream.of(
                Arguments.of("Product 1", "Description", BigDecimal.valueOf(10.0), 10),
                Arguments.of("Product 2", "Description", BigDecimal.valueOf(5), 1),
                Arguments.of("Product 3", "Description", BigDecimal.valueOf(2), 2),
                Arguments.of("Product 4", "Description", BigDecimal.valueOf(12.13), 5),
                Arguments.of("Product 5", "Description", BigDecimal.valueOf(2.3), 20)
        );
    }

    @Test
    public void it_should_save_product() {
        // given
        Product product = Product.builder()
                .name("Product 1")
                .description("Description")
                .price(BigDecimal.valueOf(10.0))
                .quantity(10)
                .build();

        // when
        Product savedProduct = productRepository.save(product);
        Product expectedProduct = testEntityManager.find(Product.class, savedProduct.getId());

        // then
        assertNotNull(expectedProduct, "Returned must not be null");
        assertEquals(savedProduct.getId(), expectedProduct.getId(), "Id must be equal");

        testEntityManager.remove(expectedProduct);
        testEntityManager.flush();
    }

    @ParameterizedTest
    @MethodSource("product_request")
    public void it_should_save_products(String name, String description, BigDecimal price, Integer quantity) {
        // given
        Product product = Product.builder()
                .name(name)
                .description(description)
                .price(price)
                .quantity(quantity)
                .build();

        // then
        Product savedProduct = productRepository.save(product);
        Product expectedProduct = testEntityManager.find(Product.class, savedProduct.getId());

        // then
        assertNotNull(expectedProduct, "Returned must not be null");
        assertEquals(savedProduct.getId(), expectedProduct.getId(), "Id must be equal");

        testEntityManager.remove(expectedProduct);
        testEntityManager.flush();
    }

    @Test
    public void it_should_return_product_of_that_id() {
        // given
        Product product = Product.builder()
                .name("Product 1")
                .description("Description")
                .price(BigDecimal.valueOf(10.0))
                .quantity(10)
                .build();
        Object id = testEntityManager.persistAndGetId(product);

        // when
        Optional<Product> expectedProduct = productRepository.findById((Long) id);

        // then
        assertTrue(expectedProduct.isPresent(), "Returned must not be null");
        assertEquals(product.getId(), expectedProduct.get().getId(), "Id must be equal");

        testEntityManager.remove(product);
        testEntityManager.flush();
    }


    @Test
    public void it_should_delete_product_with_id() {
        // given
        Product product = Product.builder()
                .name("Product 1")
                .description("Description")
                .price(BigDecimal.valueOf(10.0))
                .quantity(10)
                .build();
        Object id = testEntityManager.persistAndGetId(product);

        // when
        productRepository.deleteById((Long) id);

        // then
        assertNull(testEntityManager.find(Customer.class, id), "Returned must be null");
    }

    @Test
    public void it_should_return_list_of_all_products() {
        // given
        Product product = Product.builder()
                .name("Product 1")
                .description("Description")
                .price(BigDecimal.valueOf(10.0))
                .quantity(10)
                .build();
        testEntityManager.persistAndFlush(product);

        // when
        List<Product> expectedProducts = productRepository.findAll();

        // then
        assertNotNull(expectedProducts, "Returned must not be null");
        assertNotEquals(0, expectedProducts.size(), "Size must not be 0");

        testEntityManager.remove(product);
        testEntityManager.flush();
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
