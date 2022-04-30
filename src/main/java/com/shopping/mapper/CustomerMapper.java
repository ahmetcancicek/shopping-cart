package com.shopping.mapper;

import com.shopping.dto.CustomerPayload;
import com.shopping.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CustomerMapper {

    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "user.password", target = "password")
    @Mapping(source = "user.username", target = "username")
    CustomerPayload toCustomerPayload(Customer customer);

    @Mapping(source = "email", target = "user.email")
    @Mapping(source = "username", target = "user.username")
    @Mapping(source = "password", target = "user.password")
    Customer toCustomer(CustomerPayload customerPayload);

    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "user.password", target = "password")
    @Mapping(source = "user.username", target = "username")
    List<CustomerPayload> toCustomerPayloads(List<Customer> customers);

    @Mapping(source = "email", target = "user.email")
    @Mapping(source = "username", target = "user.username")
    @Mapping(source = "password", target = "user.password")
    List<Customer> toCustomers(List<CustomerPayload> customerPayloads);
}
