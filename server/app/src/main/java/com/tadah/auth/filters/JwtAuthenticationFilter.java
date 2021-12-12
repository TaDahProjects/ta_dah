package com.tadah.auth.filters;

import com.tadah.auth.applications.AuthenticationService;
import com.tadah.auth.applications.AuthorizationService;
import com.tadah.auth.authentications.UserAuthentication;
import com.tadah.auth.domains.entities.Role;
import com.tadah.user.domains.entities.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 인증을 진행한다.
 */
public final class JwtAuthenticationFilter extends BasicAuthenticationFilter {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    private final AuthorizationService authorizationService;
    private final AuthenticationService authenticationService;
    public JwtAuthenticationFilter(
        final AuthenticationManager authenticationManager,
        final AuthorizationService authorizationService,
        final AuthenticationService authenticationService
        ) {
        super(authenticationManager);
        this.authorizationService = authorizationService;
        this.authenticationService = authenticationService;
    }

    @Override
    protected void doFilterInternal(
        final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain
    ) throws IOException, ServletException {
        final String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (authorizationHeader != null) {
            final String token = authorizationHeader.substring(TOKEN_PREFIX.length());
            final User user = authenticationService.verify(token);
            final List<Role> roles = authorizationService.list(user.getId());
            final Authentication authentication = new UserAuthentication(user, roles);

            final SecurityContext context = SecurityContextHolder.getContext();
            context.setAuthentication(authentication);
        }
        super.doFilterInternal(request, response, chain);
    }
}
