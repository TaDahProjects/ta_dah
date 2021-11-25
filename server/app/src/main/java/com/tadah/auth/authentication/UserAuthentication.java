package com.tadah.auth.authentication;

import com.tadah.common.annotations.Generated;
import com.tadah.user.domain.entities.User;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public final class UserAuthentication extends AbstractAuthenticationToken {
    private static List<GrantedAuthority> authorities(final User user) {
        return List.of(user.getUserType());
    }

    private final User user;

    public UserAuthentication(final User user) {
        super(authorities(user));
        super.setAuthenticated(true);
        this.user = user;
    }

    @Generated
    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return user;
    }
}
