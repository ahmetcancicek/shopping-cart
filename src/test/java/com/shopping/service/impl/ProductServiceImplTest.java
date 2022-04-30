package com.shopping.service.impl;

import com.shopping.dto.ProductPayload;
import com.shopping.exception.NoSuchElementFoundException;
import com.shopping.mapper.ProductMapper;
import com.shopping.model.Product;
import com.shopping.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    public static Stream<Arguments> product_requests() {
        return Stream.of(
                Arguments.of(1L, "Egg", "KL412BA", "Description", BigDecimal.valueOf(10.0), 10),
                Arguments.of(2L, "Salt", "TBA374", "Description", BigDecimal.valueOf(5), 1),
                Arguments.of(3L, "Juice", "PDV2382", "Description", BigDecimal.valueOf(2), 2),
                Arguments.of(4L, "Cola", "LASGB32", "Description", BigDecimal.valueOf(12.13), 5),
                Arguments.of(5L, "Bread", "KLAS452", "Description", BigDecimal.valueOf(2.3), 20)
        );
    }

    @Test
    public void it_should_save_product() {
        // given
        Product product = Product.builder()
                .id(1L)
                .serialNumber("Y5N3DJ")
                .name("Egg")
                .description("Super egg")
                .price(BigDecimal.valueOf(10.0))
                .quantity(10)
                .build();

        given(productRepository.save(any())).willReturn(product);

        // when
        ProductPayload savedProduct = productService.save(ProductMapper.INSTANCE.toProductPayload(product));

        // then
        verify(productRepository, times(1)).save(any());
        assertNotNull(savedProduct, "Returned must not be null");
        assertEquals(product.getSerialNumber(), savedProduct.getSerialNumber(), "Serial number must be equal");
    }


    @ParameterizedTest
    @MethodSource("product_requests")
    public void it_should_save_products(Long id, String serialNumber, String name, String description, BigDecimal price, Integer quantity) {
        // given
        Product product = Product.builder()
                .id(id)
                .serialNumber(serialNumber)
                .name(name)
                .description(description)
                .price(price)
                .quantity(quantity)
                .build();

        given(productRepository.save(any())).willReturn(product);

        // when
        ProductPayload savedProduct = productService.save(ProductMapper.INSTANCE.toProductPayload(product));

        // then
        verify(productRepository, times(1)).save(any());
        assertNotNull(savedProduct, "Returned must not be null");
        assertEquals(product.getSerialNumber(), savedProduct.getSerialNumber(), "Serial number must be equal");
    }

    @Test
    public void it_should_update_product() {
        // given
        Product product = Product.builder()
                .id(1L)
                .serialNumber("Y5N3DJ")
                .name("Egg")
                .description("Super egg")
                .price(BigDecimal.valueOf(10.0))
                .quantity(10)
                .build();

        Product productUpdate = Product.builder()
                .id(1L)
                .serialNumber("Y5N3DJ")
                .name("Egg")
                .description("Super egg")
                .price(BigDecimal.valueOf(10.0))
                .quantity(2)
                .build();

        given(productRepository.findBySerialNumber(any())).willReturn(Optional.of(product));
        given(productRepository.save(any())).willReturn(productUpdate);

        // when
        ProductPayload updatedProduct = productService.update(ProductMapper.INSTANCE.toProductPayload(product));

        // then
        assertNotNull(updatedProduct, "Returned must not be null");
        assertEquals(product.getSerialNumber(), updatedProduct.getSerialNumber(), "Serial number must be equal");
    }

    @Test
    public void it_should_throw_exception_when_update_product_that_does_not_exist() {
        // given
        Product product = Product.builder()
                .id(1L)
                .serialNumber("Y5N3DJ")
                .name("Egg")
                .description("Super egg")
                .price(BigDecimal.valueOf(10.0))
                .quantity(10)
                .build();

        given(productRepository.findBySerialNumber(any())).willThrow(new NoSuchElementFoundException("product does not exist"));

        // when
        Throwable throwable = catchThrowable(() -> {
            productService.update(ProductMapper.INSTANCE.toProductPayload(product));
        });

        // then
        assertThat(throwable).isInstanceOf(NoSuchElementFoundException.class);
    }

    @Test
    public void it_should_return_product_of_that_id() {
        // given
        Product product = Product.builder()
                .id(1L)
                .serialNumber("Y5N3DJ")
                .name("Egg")
                .description("Super egg")
                .price(BigDecimal.valueOf(10.0))
                .quantity(10)
                .build();

        given(productRepository.findById(product.getId())).willReturn(Optional.of(product));

        // when
        ProductPayload expectedProduct = productService.findById(product.getId());

        // then
        verify(productRepository, times(1)).findById(any());
        assertNotNull(expectedProduct, "Returned must not be null");
        assertEquals(product.getSerialNumber(), expectedProduct.getSerialNumber(), "Serial number must be equal");
    }

    @Test
    public void it_should_return_product_of_that_serialNumber() {
        // given
        Product product = Product.builder()
                .id(1L)
                .serialNumber("Y5N3DJ")
                .name("Egg")
                .description("Super egg")
                .price(BigDecimal.valueOf(10.0))
                .quantity(10)
                .build();

        given(productRepository.findBySerialNumber(any())).willReturn(Optional.of(product));

        // when
        ProductPayload expectedProductPayload = productService.findBySerialNumber(product.getSerialNumber());

        // then
        verify(productRepository, times(1)).findBySerialNumber(any());
        assertNotNull(expectedProductPayload, "Returned must not be null");
        assertEquals(product.getSerialNumber(), expectedProductPayload.getSerialNumber(), "Serial number must be equal");
    }

    @Test
    public void it_should_delete_product_of_that_id() {
        // given
        Product product = Product.builder()
                .id(1L)
                .serialNumber("Y5N3DJ")
                .name("Egg")
                .description("Super egg")
                .price(BigDecimal.valueOf(10.0))
                .quantity(10)
                .build();

        given(productRepository.findById(any())).willReturn(Optional.of(product));

        // when
        productService.deleteById(product.getId());

        // then
        verify(productRepository, times(1)).deleteById(any());
    }

    @Test
    public void it_should_delete_product_of_that_serialNumber() {
        // given
        Product product = Product.builder()
                .id(1L)
                .serialNumber("Y5N3DJ")
                .name("Egg")
                .description("Super egg")
                .price(BigDecimal.valueOf(10.0))
                .quantity(10)
                .build();

        given(productRepository.findBySerialNumber(any())).willReturn(Optional.of(product));

        // when
        productService.deleteBySerialNumber(product.getSerialNumber());

        // then
        verify(productRepository, times(1)).deleteBySerialNumber(any());
    }

    @Test
    public void it_should_throw_exception_when_delete_product_that_does_not_exist() {
        // given
        when(productRepository.findById(any())).thenThrow(new NoSuchElementFoundException("product does not exist"));

        // when
        Throwable throwable = catchThrowable(() -> {
            productService.deleteById(1L);
        });

        // then
        assertThat(throwable).isInstanceOf(NoSuchElementFoundException.class);

    }

    @Test
    public void it_should_return_list_of_all_products() {
        // given
        List<Product> products = new ArrayList<>();
        products.add(Product.builder()
                .id(1L)
                .serialNumber("Y5N3DJ")
                .name("Egg")
                .description("Super egg")
                .price(BigDecimal.valueOf(10.0))
                .quantity(10)
                .build());

        products.add(Product.builder()
                .id(2L)
                .serialNumber("Y5N3DJ")
                .name("Bread")
                .description("Super bread")
                .price(BigDecimal.valueOf(5.0))
                .quantity(2)
                .build());

        given(productRepository.findAll()).willReturn(products);

        // when
        List<ProductPayload> expectedProducts = productService.findAll();

        // then
        verify(productRepository, times(1)).findAll();
        assertNotNull(expectedProducts, "List must not be null");
        assertEquals("Egg", expectedProducts.get(0).getName(), "Name must be equal");
    }

    @Test
    public void it_should_return_list_of_products_with_pageable() {
        // TODO: Fix findAll test for product
//        // given
//        List<Product> products = new ArrayList<>();
//        products.add(Product.builder()
//                .id(1L)
//                .name("Product 1")
//                .description("Description")
//                .price(BigDecimal.valueOf(10.0))
//                .quantity(10).build());
//
//        products.add(Product.builder()
//                .id(2L)
//                .name("Product 2")
//                .description("Description")
//                .price(BigDecimal.valueOf(5.0))
//                .quantity(2)
//                .build());
//
//        Page<Product> productPage = new PageImpl<>(
//                products.subList(0, 2),
//                PageRequest.of(0, 2),
//                products.size());
//        given(productRepository.findAll(any(Pageable.class))).willReturn(productPage);
//
//        // when
//        Page<Product> expectedProducts = productService.findAll(PageRequest.of(0, 2));
//
//        // then
//        verify(productRepository, times(1)).findAll(PageRequest.of(0, 2));
//        assertNotNull(expectedProducts, "List must not be null");
//        assertEquals(2, expectedProducts.getTotalElements(), "Total element must be equal");
    }
}