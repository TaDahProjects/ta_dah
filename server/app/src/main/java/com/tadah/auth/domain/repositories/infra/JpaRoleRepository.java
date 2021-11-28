package com.tadah.auth.domain.repositories.infra;

import com.tadah.auth.domain.entities.Role;
import com.tadah.auth.domain.repositories.RoleRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaRoleRepository extends RoleRepository, JpaRepository<Role, Long> {
}
