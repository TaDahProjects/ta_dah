package com.tadah.user.domain;

import org.springframework.security.core.GrantedAuthority;

public enum UserType implements GrantedAuthority {
    RIDER,
    DRIVER;

    @Override
    public String getAuthority() {
        return name();
    }
}
