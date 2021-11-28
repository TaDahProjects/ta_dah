package com.tadah.auth.applications;

import com.tadah.auth.domain.entities.Role;
import com.tadah.auth.domain.repositories.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 권한 생성 및 조회를 수행한다.
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

    /**
     * 권한을 목록을 조회한다.
     *
     * @param userId 조회할 대상
     * @return 조회한 권한 목록
     */
    public List<Role> list(final Long userId) {
        return roleRepository.findAllByUserId(userId);
    }
}
