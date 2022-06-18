package com.shopping.service.impl;

import com.shopping.domain.exception.AlreadyExistsElementException;
import com.shopping.domain.exception.NoSuchElementFoundException;
import com.shopping.domain.model.Role;
import com.shopping.repository.RoleRepository;
import com.shopping.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role save(Role role) {
        roleRepository.findByName(role.getName()).ifPresent((it) -> {
            log.error("role already exists with name: {}", it.getName());
            throw new AlreadyExistsElementException(String.format("role already exist with name: %s", it.getName()));
        });
        return roleRepository.save(role);
    }

    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name).orElseThrow(() -> {
            log.error("role does not exist with name: {}", name);
            throw new NoSuchElementFoundException(String.format("role does not exist with name: %s", name));
        });
    }
}
