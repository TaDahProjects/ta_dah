package com.tadah.auth.domain.repositories;

import com.tadah.auth.domain.entities.Role;

import java.util.Optional;

public interface RoleRepository {
    /**
     * 사용자 권한을 저장한다.
     *
     * @param role 저장할 사용자 권한
     * @return 저장한 사용자 권한
     */
    Role save(final Role role);

    /**
     * 사용자의 권한을 조회한다.
     *
     * @param userId 권한을 조회할 사용자
     * @return 권한
     */
    Optional<Role> findByUserId(final Long userId);
}
