package com.enigma.orderin.service.impl;

import com.enigma.orderin.entity.Role;
import com.enigma.orderin.repository.RoleRepository;
import com.enigma.orderin.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    @Override
    public Role getOrSave(Role role) {
        Role optionalRole = roleRepository.findByName(role.getName());
        if (optionalRole == null) {
            return roleRepository.save(role);
        }
        return null;
    }
}
