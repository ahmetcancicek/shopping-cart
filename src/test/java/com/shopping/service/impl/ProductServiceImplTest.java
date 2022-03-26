package com.shopping.service.impl;

import com.shopping.model.Product;
import com.shopping.repository.ProductRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @Test
    public void should_return_product_of_that_id_when_called_findById() {
        Product product = Product.builder()
                .id(1L)
                .name("Product 1")
                .description("Description")
                .price(BigDecimal.valueOf(10.0))
                .quantity(10)
                .build();

        given(productRepository.findById(product.getId())).willReturn(Optional.of(product));

        Optional<Product> expected = productService.findById(product.getId());

        verify(productRepository, times(1)).findById(any());
        assertTrue(expected.isPresent(), "Returned must not be null");
        assertEquals(product.getId(), expected.get().getId(), "Id must be equal");
    }

    @Test
    public void should_save_product() {
        Product product = Product.builder()
                .id(1L)
                .name("Product 1")
                .description("Description")
                .price(BigDecimal.valueOf(10.0))
                .quantity(10)
                .build();

        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product savedProduct = productService.save(product);

        verify(productRepository, times(1)).save(any());
        assertNotNull(savedProduct, "Must not be null");
        assertEquals(product.getId(), savedProduct.getId(), "Id must be equal");
    }

    @Test
    public void should_delete_product_with_id() {
        Long productId = 1L;

        productService.deleteById(productId);
        productService.deleteById(productId);

        verify(productRepository, times(2)).deleteById(productId);
    }

    @Test
    public void should_return_list_of_all_products() {
        Product productOne = Product.builder()
                .id(1L)
                .name("Product 1")
                .description("Description")
                .price(BigDecimal.valueOf(10.0))
                .quantity(10)
                .build();

        Product productTwo = Product.builder()
                .id(2L)
                .name("Product 2")
                .description("Description")
                .price(BigDecimal.valueOf(5.0))
                .quantity(2)
                .build();

        List<Product> products = new ArrayList<>();
        products.add(productOne);
        products.add(productTwo);

        given(productRepository.findAll()).willReturn(products);

        List<Product> expectedProducts = productService.findAll();
        verify(productRepository, times(1)).findAll();
        assertNotNull(expectedProducts, "List must not be null");
        assertEquals("Product 1", expectedProducts.get(0).getName(), "Name must be equal");
    }

    @Test
    public void should_return_list_of_products_with_pageable() {
        Product productOne = Product.builder()
                .id(1L)
                .name("Product 1")
                .description("Description")
                .price(BigDecimal.valueOf(10.0))
                .quantity(10)
                .build();

        Product productTwo = Product.builder()
                .id(2L)
                .name("Product 2")
                .description("Description")
                .price(BigDecimal.valueOf(5.0))
                .quantity(2)
                .build();

        List<Product> products = new ArrayList<>();
        products.add(productOne);
        products.add(productTwo);

        Pageable firstPageWithTwoElements = PageRequest.of(0, 2);
        Page<Product> productPage = new PageImpl<>(products.subList(0, 2), firstPageWithTwoElements, products.size());

        Mockito.when(productRepository.findAll(any(Pageable.class))).thenReturn(productPage);

        Page<Product> expectedProducts = productService.findAll(firstPageWithTwoElements);
        verify(productRepository, times(1)).findAll(firstPageWithTwoElements);
        assertNotNull(expectedProducts, "List must not be null");
        assertEquals(2, expectedProducts.getTotalElements(), "Total element must be equal");
    }
}