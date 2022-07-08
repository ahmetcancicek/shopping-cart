package com.shopping.controller.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopping.controller.AddressController;
import com.shopping.domain.dto.AddressRequest;
import com.shopping.domain.dto.AddressResponse;
import com.shopping.service.AddressService;
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

@WebMvcTest(AddressController.class)
@AutoConfigureMockMvc
public class AddressControllerTest extends BaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private AddressService addressService;

    @Test
    @WithMockUser(username = "stevehouse", password = "GT380ABD")
    public void it_should_add_address() throws Exception {
        // given
        AddressRequest addressRequest = AddressRequest.builder()
                .street("43 Grand Street")
                .city("New York")
                .zipCode("00501")
                .stateCode("+1")
                .build();

        AddressResponse addressResponse = AddressResponse.builder()
                .id(1L)
                .street("43 Grand Street")
                .city("New York")
                .zipCode("00501")
                .stateCode("+1")
                .build();

        given(addressService.save(any(), any())).willReturn(addressResponse);

        // when
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .post("/api/v1/addresses")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(addressRequest));

        // then
        mockMvc.perform(mockRequest)
                .andExpect(jsonPath("$.status").value(HttpStatus.CREATED.value()))
                .andExpect(jsonPath("$.data.id").value(addressResponse.getId()))
                .andExpect(jsonPath("$.data.street").value(addressResponse.getStreet()))
                .andExpect(jsonPath("$.data.city").value(addressResponse.getCity()))
                .andExpect(jsonPath("$.data.zipCode").value(addressResponse.getZipCode()))
                .andExpect(jsonPath("$.data.stateCode").value(addressResponse.getStateCode()));
    }

    @Test
    @WithMockUser(username = "stevehouse", password = "GT380ABD")
    public void it_should_delete_address() throws Exception {
        // given
        AddressResponse addressResponse = AddressResponse.builder()
                .id(1L)
                .street("43 Grand Street")
                .city("New York")
                .zipCode("00501")
                .stateCode("+1")
                .build();

        given(addressService.findByIdAndUsername(any(), any())).willReturn(addressResponse);

        // when
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .delete("/api/v1/addresses/{id}", addressResponse.getId())
                .accept(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(mockRequest)
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()));
    }

    @Test
    @WithMockUser(username = "stevehouse", password = "GT380ABD")
    public void it_should_return_address_of_that_customer() throws Exception {
        // given
        AddressResponse addressResponse = AddressResponse.builder()
                .id(1L)
                .street("43 Grand Street")
                .city("New York")
                .zipCode("00501")
                .stateCode("+1")
                .build();

        given(addressService.findByIdAndUsername(any(), any())).willReturn(addressResponse);

        // when
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .get("/api/v1/addresses/{id}", addressResponse.getId())
                .accept(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(mockRequest)
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.data.id").value(addressResponse.getId()))
                .andExpect(jsonPath("$.data.street").value(addressResponse.getStreet()))
                .andExpect(jsonPath("$.data.city").value(addressResponse.getCity()))
                .andExpect(jsonPath("$.data.zipCode").value(addressResponse.getZipCode()))
                .andExpect(jsonPath("$.data.stateCode").value(addressResponse.getStateCode()));
    }
}
