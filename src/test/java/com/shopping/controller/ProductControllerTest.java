package com.shopping.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.shopping.exception.ProductNotFoundException;
import com.shopping.model.Product;

import com.shopping.service.ProductService;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private ProductService productService;

    @Test
    public void it_should_add_product() throws Exception {
        // given
        Product product = Product.builder()
                .id(1L)
                .name("Product 1")
                .quantity(5)
                .price(BigDecimal.valueOf(12.50))
                .build();

        when(productService.save(any())).thenReturn(product);

        // when
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(product));

        // then
        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("1"));
    }

    @Test
    public void it_should_delete_product_of_that_id() throws Exception {
        // given
        Product product = Product.builder()
                .id(1L)
                .name("Product 1")
                .quantity(5)
                .price(BigDecimal.valueOf(12.50))
                .build();

        when(productService.findById(any())).thenReturn(product);

        // when
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .delete("/products/{id}", "1");

        // then
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk());
    }

    @Test
    public void it_should_return_bad_request_when_delete_product_of_that_id_does_not_exist() throws Exception {
        // given
        Product product = Product.builder()
                .id(1L)
                .name("Product 1")
                .quantity(5)
                .price(BigDecimal.valueOf(12.50))
                .build();

        doThrow(new ProductNotFoundException("product does not exist")).when(productService).deleteById(any());

        // when
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .delete("/products/{id}", "1");

        // then
        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void it_should_return_product_of_that_id() throws Exception {
        // given
        Product product = Product.builder()
                .id(1L)
                .name("Product 1")
                .quantity(5)
                .price(BigDecimal.valueOf(12.50))
                .build();

        when(productService.findById(any())).thenReturn(product);

        // when
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .get("/products/{id}", "1")
                .accept(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void it_should_return_bad_request_when_product_does_not_exist() throws Exception {
        // given
        Product product = Product.builder()
                .id(1L)
                .name("Product 1")
                .quantity(5)
                .price(BigDecimal.valueOf(12.50))
                .build();

        doThrow(new ProductNotFoundException("product does not exist")).when(productService).findById(any());

        // when
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .get("/products/{id}", 1);

        // then
        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void it_should_return_list_of_all_products() throws Exception {
        // given
        List<Product> products = new ArrayList<>();
        products.add(Product.builder().id(1L).name("Product 1").quantity(5).price(BigDecimal.valueOf(12.50)).build());
        products.add(Product.builder().id(2L).name("Product 2").quantity(10).price(BigDecimal.valueOf(2.50)).build());

        when(productService.findAll()).thenReturn(products);

        // when
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .get("/products")
                .accept(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2))
                .andDo(print());
    }

    @Test
    public void it_should_update_product_of_that_id() throws Exception {
        // given
        Product product = Product.builder()
                .id(1L)
                .name("Product 1")
                .quantity(5)
                .price(BigDecimal.valueOf(12.50))
                .build();

        Product productUpdate = Product.builder()
                .id(1L)
                .name("Product 1")
                .quantity(2)
                .price(BigDecimal.valueOf(12.50))
                .build();

        when(productService.update(any())).thenReturn(productUpdate);
        when(productService.findById(any())).thenReturn(product);

        // when
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .put("/products")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(product));

        // then
        mockMvc.perform(mockRequest)
                .andExpect(jsonPath("$.quantity").value(2))
                .andExpect(status().isOk());
    }
}
