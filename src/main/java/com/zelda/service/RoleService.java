package com.zelda.service;

import com.zelda.model.entity.Role;

public interface RoleService {
    Iterable<Role> findAll();

    void save(Role role);

    Role findByName(String name);
}

