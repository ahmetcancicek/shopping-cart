package com.shopping.service.impl;

import com.shopping.domain.dto.AddressRequest;
import com.shopping.domain.dto.AddressResponse;
import com.shopping.domain.model.Address;
import com.shopping.domain.model.Customer;
import com.shopping.domain.model.User;
import com.shopping.repository.AddressRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AddressServiceImplTest {

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private AddressServiceImpl addressService;

    @Mock
    private CustomerServiceImpl customerService;

    @Test
    public void it_should_add_address() {
        // given
        Customer customer = Customer.builder()
                .id(1L)
                .firstName("Lucy")
                .lastName("King")
                .user(User.builder()
                        .id(1L)
                        .active(true)
                        .username("lucyking")
                        .email("lucyking@email.com")
                        .password("LA4SD12")
                        .build())
                .build();

        Address address = Address.builder()
                .id(1L)
                .street("43 Grand Street")
                .city("New York")
                .zipCode("00501")
                .stateCode("+1")
                .customer(customer)
                .build();

        AddressRequest addressRequest = AddressRequest.builder()
                .street("43 Grand Street")
                .city("New York")
                .zipCode("00501")
                .stateCode("+1")
                .build();

        given(customerService.findCustomerByUsername(any())).willReturn(customer);
        given(addressRepository.save(any())).willReturn(address);

        // when
        AddressResponse expectedAddressResponse = addressService.save(customer.getUser().getUsername(), addressRequest);

        // then
        verify(addressRepository, times(1)).save(any());
        assertEquals(addressRequest.getStateCode(), expectedAddressResponse.getStateCode(), "State code must be equal");
        assertEquals(addressRequest.getStreet(), expectedAddressResponse.getStreet(), "Street must be equal");
        assertEquals(addressRequest.getCity(), expectedAddressResponse.getCity(), "City must be equal");
        assertEquals(addressRequest.getZipCode(), expectedAddressResponse.getZipCode(), "Zip code must be equal");
        assertEquals(addressRequest.getStateCode(), expectedAddressResponse.getStateCode(), "State code must be equal");
    }

    @Test
    public void it_should_delete_address() {
        // given
        Customer customer = Customer.builder()
                .id(1L)
                .firstName("Lucy")
                .lastName("King")
                .user(User.builder()
                        .id(1L)
                        .active(true)
                        .username("lucyking")
                        .email("lucyking@email.com")
                        .password("LA4SD12")
                        .build())
                .build();

        Address address = Address.builder()
                .id(1L)
                .street("43 Grand Street")
                .city("New York")
                .zipCode("00501")
                .stateCode("+1")
                .customer(customer)
                .build();

        given(addressRepository.findByIdAndCustomer_User_Username(any(), any())).willReturn(Optional.of(address));

        // when
        addressService.deleteByIdAndUsername(address.getId(), customer.getUser().getUsername());

        // then
        verify(addressRepository, times(1)).deleteById(any());
    }

    @Test
    public void it_should_return_address_of_that_customer() {
        // given
        Customer customer = Customer.builder()
                .id(1L)
                .firstName("Lucy")
                .lastName("King")
                .user(User.builder()
                        .id(1L)
                        .active(true)
                        .username("lucyking")
                        .email("lucyking@email.com")
                        .password("LA4SD12")
                        .build())
                .build();

        Address address = Address.builder()
                .id(1L)
                .street("43 Grand Street")
                .city("New York")
                .zipCode("00501")
                .stateCode("+1")
                .customer(customer)
                .build();

        given(addressRepository.findByIdAndCustomer_User_Username(any(), any())).willReturn(Optional.of(address));

        // when
        AddressResponse expectedAddressResponse = addressService.findByIdAndUsername(address.getId(), customer.getUser().getUsername());

        // then
        verify(addressRepository, times(1)).findByIdAndCustomer_User_Username(any(), any());
        assertEquals(address.getStateCode(), expectedAddressResponse.getStateCode(), "State code must be equal");
        assertEquals(address.getStreet(), expectedAddressResponse.getStreet(), "Street must be equal");
        assertEquals(address.getCity(), expectedAddressResponse.getCity(), "City must be equal");
        assertEquals(address.getZipCode(), expectedAddressResponse.getZipCode(), "Zip code must be equal");
        assertEquals(address.getStateCode(), expectedAddressResponse.getStateCode(), "State code must be equal");

    }
}
