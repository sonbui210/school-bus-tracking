package com.hoanganhbk.schoolBus.data.repository;

import com.hoanganhbk.schoolBus.data.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByCode(String code);
}
