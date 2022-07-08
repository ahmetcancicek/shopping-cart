package com.shopping.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopping.controller.PaymentMethodController;
import com.shopping.domain.dto.PaymentMethodRequest;
import com.shopping.domain.dto.PaymentMethodResponse;
import com.shopping.domain.model.PaymentType;
import com.shopping.service.PaymentMethodService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(PaymentMethodController.class)
@AutoConfigureMockMvc
public class PaymentMethodControllerTest extends BaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private PaymentMethodService paymentMethodService;

    @Test
    @WithMockUser(username = "stevehouse", password = "GT380ABD")
    public void it_should_add_payment_method() throws Exception {
        // given
        PaymentMethodRequest paymentMethodRequest = PaymentMethodRequest.builder()
                .name("My VISA")
                .paymentType(PaymentType.VISA)
                .build();

        PaymentMethodResponse paymentMethodResponse = PaymentMethodResponse.builder()
                .id(1L)
                .name("My VISA")
                .paymentType(PaymentType.VISA)
                .build();

        given(paymentMethodService.save(any(), any())).willReturn(paymentMethodResponse);

        // when
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .post("/api/v1/paymentmethod")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(paymentMethodRequest));

        // then
        mockMvc.perform(mockRequest)
                .andExpect(jsonPath("$.status").value(HttpStatus.CREATED.value()))
                .andExpect(jsonPath("$.data.name").value("My VISA"))
                .andExpect(jsonPath("$.data.paymentType").value("VISA"));
    }

    @Test
    @WithMockUser(username = "stevehouse", password = "GT380ABD")
    public void it_should_delete_payment_method() throws Exception {
        // given
        PaymentMethodResponse paymentMethodResponse = PaymentMethodResponse.builder()
                .id(1L)
                .name("My VISA")
                .paymentType(PaymentType.VISA)
                .build();

        given(paymentMethodService.findByIdAndUsername(any(), any())).willReturn(paymentMethodResponse);

        // when
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .delete("/api/v1/paymentmethod/{id}", paymentMethodResponse.getId())
                .accept(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(mockRequest)
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()));
    }

    @Test
    @WithMockUser(username = "stevehouse", password = "GT380ABD")
    public void it_should_return_payment_method_of_that_username() throws Exception {
        // given
        PaymentMethodResponse paymentMethodResponse = PaymentMethodResponse.builder()
                .id(1L)
                .name("My VISA")
                .paymentType(PaymentType.VISA)
                .build();

        given(paymentMethodService.findByIdAndUsername(any(), any())).willReturn(paymentMethodResponse);

        // when
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .get("/api/v1/paymentmethod/{id}", paymentMethodResponse.getId())
                .accept(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(mockRequest)
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.data.id").value("1"))
                .andExpect(jsonPath("$.data.name").value("My VISA"))
                .andExpect(jsonPath("$.data.paymentType").value("VISA"));
    }

}
