package com.tadah.auth.domain.entities;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 사용자의 권한을 정의한다.
 */
@Generated
@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@RequiredArgsConstructor
public final class Role {
    @Id
    @GeneratedValue
    private Long id;

    private Long userId;

    private String name;

    public Role(final Long userId, final String name) {
        this.userId = userId;
        this.name = name;
    }
}
