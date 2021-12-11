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

    private Page<Product> productPage;

    @BeforeAll
    static void beforeAll() {
        Page<Product> pro = Mockito.mock(Page.class);
    }

    @Test
    public void should_return_product_of_that_id_when_called_findById() {
        final Product product = new Product(1L, "Product 1", "Description", new BigDecimal("10.00"), 10);

        given(productRepository.findById(product.getId())).willReturn(Optional.of(product));

        final Optional<Product> expected = productService.findById(product.getId());

        assertTrue(expected.isPresent(), "Returned must not be null");
        assertEquals(product.getId(), expected.get().getId(), "Id must be equal");
    }

    @Test
    public void should_save_product() {
        final Product product = new Product(1L, "Product 1", "Description", new BigDecimal("10.00"), 10);

        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product savedProduct = productService.save(product);

        verify(productRepository, times(1)).save(any());
        assertNotNull(savedProduct, "Must not be null");
    }

    @Test
    public void should_delete_product_when_delete_product_with_id() {
        final Long productId = 1L;

        productService.deleteById(productId);
        productService.deleteById(productId);

        verify(productRepository, times(2)).deleteById(productId);
    }

    @Test
    public void should_return_list_of_all_products() {
        final Product product1 = new Product(1L, "Product 1", "Description", new BigDecimal("10.00"), 10);
        final Product product2 = new Product(1L, "Product 2", "Description", new BigDecimal("10.00"), 10);

        List<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);

        given(productRepository.findAll()).willReturn(products);

        final List<Product> expectedProducts = productService.findAll();

        assertEquals("Product 1", expectedProducts.get(0).getName(), "Name must be equal");
    }

    @Test
    public void should_return_list_of_products_with_pageable() {
        final Product product1 = new Product(1L, "Product 1", "Description", new BigDecimal("10.00"), 10);
        final Product product2 = new Product(1L, "Product 2", "Description", new BigDecimal("10.00"), 10);

        List<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);

        Pageable firstPageWithTwoElements = PageRequest.of(0, 2);
        productPage = new PageImpl<>(products.subList(0, 2), firstPageWithTwoElements, products.size());

        Mockito.when(productRepository.findAll(any(Pageable.class))).thenReturn(productPage);

        final Page<Product> expectedProducts = productService.findAll(firstPageWithTwoElements);

        assertEquals(2, expectedProducts.getTotalElements(), "Total element must be equal");

    }
}