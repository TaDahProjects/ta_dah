package com.tadah.auth.authentication;

import com.tadah.auth.domain.entities.Role;
import com.tadah.common.annotations.Generated;
import com.tadah.user.domain.entities.User;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.List;

/**
 * 인증 여부 및 권한 목록을 저장한다.
 */
public final class UserAuthentication extends AbstractAuthenticationToken {
    private final User user;
    public UserAuthentication(final User user, final List<Role> roles) {
        super(roles);
        super.setAuthenticated(true);
        this.user = user;
    }

    @Generated
    @Override
    public Object getCredentials() {
        return null;
    }

    /**
     * 인증된 사용자 정보를 리턴한다.
     *
     * @return 인증된 사용자 정보
     */
    @Override
    public Object getPrincipal() {
        return user;
    }
}
