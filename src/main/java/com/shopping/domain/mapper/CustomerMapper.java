package com.shopping.domain.mapper;

import com.shopping.domain.dto.RegistrationRequest;
import com.shopping.domain.dto.CustomerResponse;
import com.shopping.domain.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CustomerMapper {

    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    @Mappings({
            @Mapping(source = "email", target = "user.email"),
            @Mapping(source = "username", target = "user.username"),
            @Mapping(source = "password", target = "user.password")
    })
    Customer toCustomer(RegistrationRequest customerRequest);

    @Mappings({
            @Mapping(source = "user.email", target = "email"),
            @Mapping(source = "user.username", target = "username")
    })
    CustomerResponse fromCustomer(Customer customer);

    @Mappings({
            @Mapping(source = "user.email", target = "email"),
            @Mapping(source = "user.username", target = "username")
    })
    List<CustomerResponse> fromCustomers(List<Customer> customers);
}
