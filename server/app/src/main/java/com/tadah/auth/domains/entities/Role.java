package com.tadah.auth.domains.entities;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 사용자의 권한을 정의한다.
 */
@Generated
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public final class Role implements GrantedAuthority {
    @Id
    @GeneratedValue
    private Long id;

    private Long userId;

    private String name;

    public Role(final Long userId, final String name) {
        this.userId = userId;
        this.name = name;
    }

    @Override
    public String getAuthority() {
        return name;
    }
}
