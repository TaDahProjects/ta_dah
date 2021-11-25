package com.tadah.auth.filters;

import com.tadah.auth.applications.AuthenticationService;
import com.tadah.auth.authentication.UserAuthentication;
import com.tadah.user.domain.entities.User;
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

/**
 * 인증을 진행한다.
 */
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    private final AuthenticationService authenticationService;
    public JwtAuthenticationFilter(
        final AuthenticationManager authenticationManager, final AuthenticationService authenticationService
        ) {
        super(authenticationManager);
        this.authenticationService = authenticationService;
    }

    @Override
    protected void doFilterInternal(
        final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain
    ) throws IOException, ServletException {
        final String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (authorizationHeader != null) {
            final String token = authorizationHeader.substring(TOKEN_PREFIX.length());
            final User user = authenticationService.verifyToken(token);
            final Authentication authentication = new UserAuthentication(user);

            final SecurityContext context = SecurityContextHolder.getContext();
            context.setAuthentication(authentication);
        }
        super.doFilterInternal(request, response, chain);
    }
}
