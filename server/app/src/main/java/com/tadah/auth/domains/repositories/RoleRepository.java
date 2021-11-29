package com.tadah.auth.domains.repositories;

import com.tadah.auth.domains.entities.Role;

import java.util.List;

public interface RoleRepository {
    /**
     * 사용자 권한을 저장한다.
     *
     * @param role 저장할 사용자 권한
     * @return 저장한 사용자 권한
     */
    Role save(final Role role);

    /**
     * 사용자의 권한 목록을 조회한다.
     *
     * @param userId 권한을 조회할 사용자
     * @return 사용자가 가지고있는 권한 목록
     */
    List<Role> findAllByUserId(final Long userId);
}
