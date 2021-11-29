package com.tadah.user.domains.repositories;

import com.tadah.user.domains.entities.User;

import java.util.Optional;

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

    /**
     * 이메일에 해당하는 사용자 데이터를 리턴한다.
     *
     * @param email 사용자의 이메일
     * @return 이메일에 해당하는 사용자 데이터
     */
    Optional<User> findByEmail(final String email);

    /**
     * id에 해당하는 사용자 데이터를 리턴합니다.
     *
     * @param id 사용자 아이디
     * @return 아이디에 해당하는 사용자 데이터
     */
    Optional<User> findById(final Long id);
}
