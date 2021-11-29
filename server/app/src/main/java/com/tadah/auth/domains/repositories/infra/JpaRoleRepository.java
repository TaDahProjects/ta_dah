package com.tadah.auth.domains.repositories.infra;

import com.tadah.auth.domains.entities.Role;
import com.tadah.auth.domains.repositories.RoleRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaRoleRepository extends RoleRepository, JpaRepository<Role, Long> {
}
