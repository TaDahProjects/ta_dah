package com.tadah.auth.filters;

import com.tadah.auth.exceptions.InvalidTokenException;
import org.springframework.http.HttpStatus;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JwtAuthenticationFilter에서 던져진 예외를 처리한다.
 */
public class AuthenticationErrorFilter extends HttpFilter {
    @Override
    protected void doFilter(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final FilterChain chain
    ) throws IOException, ServletException {
        try {
            super.doFilter(request, response, chain);
        } catch (InvalidTokenException exception) {
            response.sendError(HttpStatus.UNAUTHORIZED.value());
        }
    }
}
