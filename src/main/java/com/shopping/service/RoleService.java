package com.shopping.service;

import com.shopping.domain.model.Role;

public interface RoleService {
    Role save(Role role);

    Role findByName(String name);
}
