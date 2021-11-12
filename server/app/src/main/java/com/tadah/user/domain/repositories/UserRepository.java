package com.tadah.user.domain.repositories;

import com.tadah.user.domain.entities.User;

public interface UserRepository {
    /**
     * 사용자 정보를 저장한다.
     *
     * @param user 저장할 사용자 정보
     * @return 저장한 사용자 정보
     */
    User save(final User user);

    /**
     * 이메일에 해당하는 사용자 존재여부 리턴한다.
     *
     * @param email 사용자의 이메일
     * @return 이메일에 해당하는 사용자 존재여부
     */
    boolean existsByEmail(final String email);
}
