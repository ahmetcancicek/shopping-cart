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

    CustomerPayload toCustomerPayload(Customer customer);

    Customer toCustomer(CustomerPayload customerPayload);

    List<CustomerPayload> toCustomerPayloads(List<Customer> customers);

    List<Customer> toCustomers(List<CustomerPayload> customerPayloads);
}
