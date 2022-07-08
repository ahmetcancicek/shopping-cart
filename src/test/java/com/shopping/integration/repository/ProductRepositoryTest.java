package com.shopping.integration.repository;

import com.shopping.domain.model.Product;
import com.shopping.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;

public class ProductRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private ProductRepository productRepository;

    public static Stream<Arguments> product_request() {
        return Stream.of(
                Arguments.of("Egg", "KL412BA", "Description", BigDecimal.valueOf(10.0), 10),
                Arguments.of("Salt", "TBA374", "Description", BigDecimal.valueOf(5), 1),
                Arguments.of("Juice", "PDV2382", "Description", BigDecimal.valueOf(2), 2),
                Arguments.of("Cola", "LASGB32", "Description", BigDecimal.valueOf(12.13), 5),
                Arguments.of("Bread", "KLAS452", "Description", BigDecimal.valueOf(2.3), 20)
        );
    }

    @Test
    public void it_should_save_product() {
        // given
        Product product = Product.builder()
                .serialNumber("Y5N3DJ")
                .name("Egg")
                .description("Super egg")
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
    public void it_should_save_products(String name, String serialNumber, String description, BigDecimal price, Integer quantity) {
        // given
        Product product = Product.builder()
                .name(name)
                .serialNumber(serialNumber)
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
    public void it_should_update_product() {
        // given
        Product product = Product.builder()
                .serialNumber("Y5N3DJ")
                .name("Egg")
                .description("Super egg")
                .price(BigDecimal.valueOf(10.0))
                .quantity(10)
                .build();

        Product savedProduct = testEntityManager.persist(product);

        // when
        savedProduct.setQuantity(2);
        productRepository.save(savedProduct);

        // then
        assertEquals(2, savedProduct.getQuantity(), "Quantity must be equal");

        testEntityManager.remove(savedProduct);
        testEntityManager.flush();
    }

    @Test
    public void it_should_return_product_of_that_id() {
        // given
        Product product = Product.builder()
                .serialNumber("Y5N3DJ")
                .name("Egg")
                .description("Super egg")
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
    public void it_should_return_product_of_that_serialNumber() {
        // given
        Product product = Product.builder()
                .serialNumber("Y5N3DJ")
                .name("Egg")
                .description("Super egg")
                .price(BigDecimal.valueOf(10.0))
                .quantity(10)
                .build();
        Object id = testEntityManager.persistAndGetId(product);

        // when
        Optional<Product> expectedProduct = productRepository.findBySerialNumber(product.getSerialNumber());

        // then
        assertTrue(expectedProduct.isPresent(), "Returned must not be null");
        assertEquals(product.getSerialNumber(), product.getSerialNumber(), "Serial number must be equal");

        testEntityManager.remove(product);
        testEntityManager.flush();
    }

    @Test
    public void it_should_delete_product_of_that_id() {
        // given
        Product product = Product.builder()
                .serialNumber("Y5N3DJ")
                .name("Egg")
                .description("Super egg")
                .price(BigDecimal.valueOf(10.0))
                .quantity(10)
                .build();
        Object id = testEntityManager.persistAndGetId(product);

        // when
        productRepository.deleteById((Long) id);

        // then
        assertNull(testEntityManager.find(Product.class, id), "Returned must be null");
    }

    @Test
    public void it_should_delete_product_of_that_serialNumber() {
        // given
        Product product = Product.builder()
                .serialNumber("Y5N3DJ")
                .name("Egg")
                .description("Super egg")
                .price(BigDecimal.valueOf(10.0))
                .quantity(10)
                .build();
        Object id = testEntityManager.persistAndGetId(product);

        // when
        productRepository.deleteBySerialNumber(product.getSerialNumber());

        // then
        assertNull(testEntityManager.find(Product.class, id), "Returned must be null");
    }

    @Test
    public void it_should_throw_exception_when_delete_product_that_does_not_exist() {
        // when
        Throwable throwable = catchThrowable(() -> {
            productRepository.deleteById(1L);
        });

        // then
        assertThat(throwable).isInstanceOf(Exception.class);
    }

    @Test
    public void it_should_return_list_of_all_products() {
        // given
        Product product = Product.builder()
                .serialNumber("Y5N3DJ")
                .name("Egg")
                .description("Super egg")
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
}
