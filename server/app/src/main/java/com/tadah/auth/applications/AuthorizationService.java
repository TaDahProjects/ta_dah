package com.tadah.auth.applications;

import com.tadah.auth.domain.entities.Role;
import com.tadah.auth.domain.repositories.RoleRepository;
import org.springframework.stereotype.Service;

/**
 * 인가를 수행한다.
 */
@Service
public class AuthorizationService {
    private final RoleRepository roleRepository;

    public AuthorizationService(final RoleRepository roleRepository) {
       this.roleRepository = roleRepository;
    }

    /**
     * 권한을 생성한다.
     *
     * @param role 생성할 권한
     * @return 생성한 권한
     */
    public Role create(final Role role) {
        return roleRepository.save(role);
    }
}
