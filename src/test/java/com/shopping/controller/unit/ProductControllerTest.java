package com.shopping.controller.unit;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.shopping.controller.ProductController;
import com.shopping.domain.dto.ProductRequest;
import com.shopping.domain.dto.ProductResponse;
import com.shopping.domain.exception.NoSuchElementFoundException;

import com.shopping.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
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
@AutoConfigureMockMvc
public class ProductControllerTest extends BaseControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private ProductService productService;

    ProductRequest productRequest;
    ProductResponse productResponse;
    List<ProductRequest> productRequests;
    List<ProductResponse> productResponses;

    @BeforeEach
    void setUp() {
        productRequest = ProductRequest.builder()
                .serialNumber("Y5N3DJ")
                .name("Egg")
                .description("Super egg")
                .price(BigDecimal.valueOf(10.0))
                .quantity(10)
                .build();

        productResponse = ProductResponse.builder()
                .serialNumber("Y5N3DJ")
                .name("Egg")
                .description("Super egg")
                .price(BigDecimal.valueOf(10.0))
                .quantity(10)
                .build();

        productRequests = new ArrayList<>(List.of(
                ProductRequest.builder()
                        .serialNumber("Y5N3DJ")
                        .name("Egg")
                        .description("Super egg")
                        .price(BigDecimal.valueOf(10.0))
                        .quantity(10)
                        .build(),
                ProductRequest.builder()
                        .serialNumber("KLN3NJ")
                        .name("Bread")
                        .description("Super bread")
                        .price(BigDecimal.valueOf(10.0))
                        .quantity(5)
                        .build()
        ));

        productResponses = new ArrayList<>(List.of(
                ProductResponse.builder()
                        .serialNumber("Y5N3DJ")
                        .name("Egg")
                        .description("Super egg")
                        .price(BigDecimal.valueOf(10.0))
                        .quantity(10)
                        .build(),
                ProductResponse.builder()
                        .serialNumber("KLN3NJ")
                        .name("Bread")
                        .description("Super bread")
                        .price(BigDecimal.valueOf(10.0))
                        .quantity(5)
                        .build()
        ));

    }

    @WithMockUser(username = "stevehouse", password = "GT380ABD", roles = {"ADMIN"})
    @Test
    public void it_should_add_product() throws Exception {
        // given
        given(productService.save(any())).willReturn(productResponse);

        // when
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(productRequest));

        // then
        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(HttpStatus.CREATED.value()))
                .andExpect(jsonPath("$.data.serialNumber").value(productResponse.getSerialNumber()))
                .andExpect(jsonPath("$.data.name").value(productResponse.getName()))
                .andExpect(jsonPath("$.data.description").value(productResponse.getDescription()))
                .andExpect(jsonPath("$.data.price").value(productResponse.getPrice()))
                .andExpect(jsonPath("$.data.quantity").value(productResponse.getQuantity()));

    }

    @WithMockUser(username = "stevehouse", password = "GT380ABD", roles = {"ADMIN"})
    @Test
    public void it_should_delete_product() throws Exception {
        // given

        given(productService.findBySerialNumber(any())).willReturn(productResponse);

        // when
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .delete("/api/v1/products/{serialNumber}", productResponse.getSerialNumber());

        // then
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()));
    }

    @WithMockUser(username = "stevehouse", password = "GT380ABD", roles = {"ADMIN"})
    @Test
    public void it_should_return_bad_request_when_delete_product_of_that_serialNumber_does_not_exist() throws Exception {
        // given
        doThrow(new NoSuchElementFoundException("product does not exist")).when(productService).deleteBySerialNumber(any());

        // when
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .delete("/api/v1/products/{serialNumber}", "Y5N3DJ");

        // then
        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andReturn();
    }

    @Test
    public void it_should_return_product() throws Exception {
        // given
        given(productService.findBySerialNumber(any())).willReturn(productResponse);

        // when
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .get("/api/v1/products/{serialNumber}", productRequest.getSerialNumber())
                .accept(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.data.serialNumber").value(productResponse.getSerialNumber()))
                .andExpect(jsonPath("$.data.name").value(productResponse.getName()))
                .andExpect(jsonPath("$.data.description").value(productResponse.getDescription()))
                .andExpect(jsonPath("$.data.price").value(productResponse.getPrice()))
                .andExpect(jsonPath("$.data.quantity").value(productResponse.getQuantity()));

    }

    @Test
    public void it_should_return_bad_request_when_product_does_not_exist() throws Exception {
        doThrow(new NoSuchElementFoundException("product does not exist")).when(productService).findBySerialNumber(any());

        // when
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .get("/api/v1/products/{serialNumber}", "Y5N3DJ");

        // then
        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()));

    }

    @Test
    public void it_should_return_list_of_all_products() throws Exception {
        // given
        given(productService.findAll()).willReturn(productResponses);

        // when
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .get("/api/v1/products")
                .accept(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.data[0].serialNumber").value(productResponse.getSerialNumber()))
                .andExpect(jsonPath("$.data[0].name").value(productResponse.getName()))
                .andExpect(jsonPath("$.data[0].description").value(productResponse.getDescription()))
                .andExpect(jsonPath("$.data[0].price").value(productResponse.getPrice()))
                .andExpect(jsonPath("$.data[0].quantity").value(productResponse.getQuantity()))
                .andDo(print());
    }

    @WithMockUser(username = "stevehouse", password = "GT380ABD", roles = {"ADMIN"})
    @Test
    public void it_should_update_product_of_that_serialNumber() throws Exception {
        // given
        productResponse.setPrice(BigDecimal.valueOf(3.0));
        productResponse.setQuantity(2);

        given(productService.update(any())).willReturn(productResponse);
        given(productService.findBySerialNumber(any())).willReturn(productResponse);

        // when
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .put("/api/v1/products")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(productRequest));

        // then
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.data.serialNumber").value(productResponse.getSerialNumber()))
                .andExpect(jsonPath("$.data.name").value(productResponse.getName()))
                .andExpect(jsonPath("$.data.description").value(productResponse.getDescription()))
                .andExpect(jsonPath("$.data.price").value(productResponse.getPrice()))
                .andExpect(jsonPath("$.data.quantity").value(productResponse.getQuantity()));
    }
}
