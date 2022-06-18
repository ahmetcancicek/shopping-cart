package com.shopping.service.impl;

import com.shopping.domain.dto.AddressRequest;
import com.shopping.domain.dto.AddressResponse;
import com.shopping.domain.exception.NoSuchElementFoundException;
import com.shopping.domain.mapper.AddressMapper;
import com.shopping.domain.model.Address;
import com.shopping.domain.model.Customer;
import com.shopping.repository.AddressRepository;
import com.shopping.service.AddressService;
import com.shopping.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final CustomerService customerService;

    @Transactional
    @Override
    public AddressResponse save(String username, AddressRequest addressRequest) {
        Customer customer = customerService.findCustomerByUsername(username);

        Address address = addressRepository.save(AddressMapper.INSTANCE.toAddress(addressRequest));
        address.setCustomer(customer);

        log.info("new address has been created with id: {}", address.getId());
        return AddressMapper.INSTANCE.fromAddress(address);

    }

    @Transactional
    @Override
    public void deleteByIdAndUsername(Long id, String username) {
        findByIdAndUsername(id, username);
        addressRepository.deleteById(id);
        log.info("address method has been deleted with şd: {}", id);
    }

    @Override
    public AddressResponse findByIdAndUsername(Long id, String username) {
        return AddressMapper.INSTANCE.fromAddress(
                addressRepository.findByIdAndCustomer_User_Username(id, username).orElseThrow(() -> {
                    log.error("address does not exist with id: {}", id);
                    return new NoSuchElementFoundException(String.format("address does not exist with id: {%s}", id));
                })
        );
    }
}
