package com.tadah.auth.configs;

import com.tadah.auth.applications.AuthenticationService;
import com.tadah.auth.applications.AuthorizationService;
import com.tadah.auth.filters.AuthenticationErrorFilter;
import com.tadah.auth.filters.JwtAuthenticationFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

/**
 * 인증 관련 설정을 등록한다.
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityJavaConfig extends WebSecurityConfigurerAdapter {
    private final AuthorizationService authorizationService;
    private final AuthenticationService authenticationService;

    public SecurityJavaConfig(
        final AuthorizationService authorizationService,
        final AuthenticationService authenticationService
    ) {
        this.authorizationService = authorizationService;
        this.authenticationService = authenticationService;
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .addFilter(new JwtAuthenticationFilter(authenticationManager(), authorizationService, authenticationService))
            .addFilterBefore(new AuthenticationErrorFilter(), JwtAuthenticationFilter.class)
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
    }
}
