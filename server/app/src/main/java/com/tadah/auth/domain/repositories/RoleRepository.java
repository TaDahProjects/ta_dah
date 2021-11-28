package com.tadah.auth.domain.repositories;

import com.tadah.auth.domain.entities.Role;

public interface RoleRepository {
    /**
     * 사용자 권한을 저장한다.
     *
     * @param role 저장할 사용자 권한
     * @return 저장한 사용자 권한
     */
    Role save(final Role role);
}
