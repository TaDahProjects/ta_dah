package com.tadah.auth.config;

import com.tadah.auth.filters.AuthenticationErrorFilter;
import com.tadah.user.applications.AuthenticationService;
import com.tadah.auth.filters.JwtAuthenticationFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

/**
 * @see <a href="https://velog.io/@dakim/EnableGlobalMethodSecurity">@EnableGlobalMethodSecurity</a>, <a herf="https://velog.io/@dakim/Spring-Security-Config">Spring Security Config</a>
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityJavaConfig extends WebSecurityConfigurerAdapter {
    private final AuthenticationService authenticationService;

    public SecurityJavaConfig(final AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .addFilter(new JwtAuthenticationFilter(authenticationManager(), authenticationService))
            .addFilterBefore(new AuthenticationErrorFilter(), JwtAuthenticationFilter.class)
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
    }
}
