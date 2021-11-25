package com.tadah.utils;

import com.tadah.auth.applications.AuthService;
import com.tadah.common.exceptions.InvalidTokenException;
import com.tadah.user.domain.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.stream.Stream;

import static com.tadah.auth.utils.JwtUtilTest.VALID_TOKEN_INVALID_CLAIMS_NAME;
import static com.tadah.auth.utils.JwtUtilTest.INVALID_TOKEN;
import static com.tadah.auth.utils.JwtUtilTest.VALID_TOKEN;
import static com.tadah.auth.utils.JwtUtilTest.VALID_TOKEN_WITHOUT_CLAIMS;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public abstract class LoginFailTest {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    private final User user;
    private final MockMvc mockMvc;
    private final MockHttpServletRequestBuilder requestBuilder;
    private final AuthService authService;

    public LoginFailTest(
        final User user,
        final MockMvc mockMvc,
        final AuthService authService,
        final MockHttpServletRequestBuilder requestBuilder
    ) {
        this.user = user;
        this.mockMvc = mockMvc;
        this.requestBuilder = requestBuilder;
        this.authService = authService;
    }

    private ResultActions subject(final MockHttpServletRequestBuilder requestBuilder) throws Exception {
        return mockMvc.perform(
                requestBuilder
                    .accept(MediaType.APPLICATION_JSON_UTF8)
                    .contentType(MediaType.APPLICATION_JSON));
    }

    @BeforeEach
    private void beforeEach() {
        when(authService.verifyToken(VALID_TOKEN))
            .thenReturn(user);
        when(authService.verifyToken(VALID_TOKEN.substring(TOKEN_PREFIX.length())))
            .thenThrow(new InvalidTokenException());
        when(authService.verifyToken(VALID_TOKEN.substring(1)))
            .thenThrow(new InvalidTokenException());
        when(authService.verifyToken(INVALID_TOKEN))
            .thenThrow(new InvalidTokenException());
        when(authService.verifyToken(VALID_TOKEN_INVALID_CLAIMS_NAME))
            .thenThrow(new InvalidTokenException());
        when(authService.verifyToken(VALID_TOKEN_WITHOUT_CLAIMS))
            .thenThrow(new InvalidTokenException());
    }

    @Nested
    @DisplayName("토큰이 없는 경우")
    public final class Context_emptyToken {
        @Test
        @DisplayName("권한이 필요함을 알려준다.")
        public void it_notifies_() throws Exception {
            subject(requestBuilder)
                .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    @DisplayName("토큰이 유효하지 않은 경우")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    public final class Context_invalidToken {
        private Stream<Arguments> methodSource() {
            return Stream.of(
                Arguments.of(VALID_TOKEN),
                Arguments.of(TOKEN_PREFIX.substring(0, TOKEN_PREFIX.length() - 1) + VALID_TOKEN),
                Arguments.of(TOKEN_PREFIX + INVALID_TOKEN),
                Arguments.of(TOKEN_PREFIX + VALID_TOKEN_INVALID_CLAIMS_NAME),
                Arguments.of(TOKEN_PREFIX + VALID_TOKEN_WITHOUT_CLAIMS)
            );
        }

        @MethodSource("methodSource")
        @DisplayName("권한이 필요함을 알려준다.")
        @ParameterizedTest(name = "header : {0}")
        public void it(final String header) throws Exception {
            subject(
                requestBuilder
                    .header(AUTHORIZATION_HEADER, header)
            ).andExpect(status().isUnauthorized());
        }
    }

    @Nested
    @DisplayName("권한이 올바르지 않은 경우")
    public final class Context_invalidAuthority {
        @Test
        @DisplayName("권한이 필요함을 알려준다.")
        public void it() throws Exception {
            subject(
                requestBuilder
                    .header(AUTHORIZATION_HEADER, TOKEN_PREFIX + VALID_TOKEN)
            ).andExpect(status().isForbidden());
        }
    }
}

