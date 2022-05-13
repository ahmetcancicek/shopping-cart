package com.shopping.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.shopping.domain.dto.ProductRequest;
import com.shopping.domain.dto.ProductResponse;
import com.shopping.domain.exception.NoSuchElementFoundException;

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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
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
        ProductRequest productRequest = ProductRequest.builder()
                .serialNumber("Y5N3DJ")
                .name("Egg")
                .description("Super egg")
                .price(BigDecimal.valueOf(10.0))
                .quantity(10)
                .build();

        ProductResponse productResponse = ProductResponse.builder()
                .serialNumber("Y5N3DJ")
                .name("Egg")
                .description("Super egg")
                .price(BigDecimal.valueOf(10.0))
                .quantity(10)
                .build();

        given(productService.save(any())).willReturn(productResponse);

        // when
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(productRequest));

        // then
        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.serialNumber").value(productRequest.getSerialNumber()));
    }

    @Test
    public void it_should_delete_product() throws Exception {
        // given
        ProductRequest productRequest = ProductRequest.builder()
                .serialNumber("Y5N3DJ")
                .name("Egg")
                .description("Super egg")
                .price(BigDecimal.valueOf(10.0))
                .quantity(10)
                .build();

        ProductResponse productResponse = ProductResponse.builder()
                .serialNumber("Y5N3DJ")
                .name("Egg")
                .description("Super egg")
                .price(BigDecimal.valueOf(10.0))
                .quantity(10)
                .build();

        given(productService.findBySerialNumber(any())).willReturn(productResponse);

        // when
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .delete("/products/{serialNumber}", productRequest.getSerialNumber());

        // then
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk());
    }

    @Test
    public void it_should_return_bad_request_when_delete_product_of_that_serialNumber_does_not_exist() throws Exception {
        // given
        doThrow(new NoSuchElementFoundException("product does not exist")).when(productService).deleteBySerialNumber(any());

        // when
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .delete("/products/{serialNumber}", "Y5N3DJ");

        // then
        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void it_should_return_product() throws Exception {
        // given
        ProductRequest productPayload = ProductRequest.builder()
                .serialNumber("Y5N3DJ")
                .name("Egg")
                .description("Super egg")
                .price(BigDecimal.valueOf(10.0))
                .quantity(10)
                .build();

        ProductResponse productResponse = ProductResponse.builder()
                .serialNumber("Y5N3DJ")
                .name("Egg")
                .description("Super egg")
                .price(BigDecimal.valueOf(10.0))
                .quantity(10)
                .build();

        given(productService.findBySerialNumber(any())).willReturn(productResponse);

        // when
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .get("/products/{serialNumber}", productPayload.getSerialNumber())
                .accept(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.serialNumber").value(productPayload.getSerialNumber()));
    }

    @Test
    public void it_should_return_bad_request_when_product_does_not_exist() throws Exception {
        doThrow(new NoSuchElementFoundException("product does not exist")).when(productService).findBySerialNumber(any());

        // when
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .get("/products/{serialNumber}", "Y5N3DJ");

        // then
        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void it_should_return_list_of_all_products() throws Exception {
        // given
        List<ProductRequest> productRequests = new ArrayList<>();
        productRequests.add(ProductRequest.builder()
                .serialNumber("Y5N3DJ")
                .name("Egg")
                .description("Super egg")
                .price(BigDecimal.valueOf(10.0))
                .quantity(10)
                .build());
        productRequests.add(ProductRequest.builder()
                .serialNumber("KLN3NJ")
                .name("Bread")
                .description("Super bread")
                .price(BigDecimal.valueOf(10.0))
                .quantity(5)
                .build());

        List<ProductResponse> productResponses = new ArrayList<>();
        productResponses.add(ProductResponse.builder()
                .serialNumber("Y5N3DJ")
                .name("Egg")
                .description("Super egg")
                .price(BigDecimal.valueOf(10.0))
                .quantity(10)
                .build());
        productResponses.add(ProductResponse.builder()
                .serialNumber("KLN3NJ")
                .name("Bread")
                .description("Super bread")
                .price(BigDecimal.valueOf(10.0))
                .quantity(5)
                .build());

        given(productService.findAll()).willReturn(productResponses);

        // when
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .get("/products")
                .accept(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].serialNumber").value("Y5N3DJ"))
                .andExpect(jsonPath("$[1].serialNumber").value("KLN3NJ"))
                .andDo(print());
    }

    @Test
    public void it_should_update_product_of_that_serialNumber() throws Exception {
        // given
        ProductRequest productRequest = ProductRequest.builder()
                .serialNumber("Y5N3DJ")
                .name("Egg")
                .description("Super egg")
                .price(BigDecimal.valueOf(10.0))
                .quantity(10)
                .build();

        ProductResponse productResponse = ProductResponse.builder()
                .serialNumber("Y5N3DJ")
                .name("Egg")
                .description("Super egg")
                .price(BigDecimal.valueOf(3.0))
                .quantity(2)
                .build();

        given(productService.update(any())).willReturn(productResponse);
        given(productService.findBySerialNumber(any())).willReturn(productResponse);

        // when
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .put("/products")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(productRequest));

        // then
        mockMvc.perform(mockRequest)
                .andExpect(jsonPath("$.quantity").value(productResponse.getQuantity()))
                .andExpect(status().isOk());
    }
}
