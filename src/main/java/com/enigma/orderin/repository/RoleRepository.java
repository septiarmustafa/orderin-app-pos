package com.enigma.orderin.repository;

import com.enigma.orderin.constant.ERole;
import com.enigma.orderin.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(ERole name);
}
