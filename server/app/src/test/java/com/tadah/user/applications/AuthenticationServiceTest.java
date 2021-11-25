package com.tadah.user.applications;

import com.tadah.common.exceptions.InvalidTokenException;
import com.tadah.user.domain.entities.User;
import com.tadah.user.domain.repositories.UserRepository;
import com.tadah.user.exceptions.LoginFailException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.tadah.user.UserConstants.EMAIL;
import static com.tadah.user.UserConstants.INVALID_EMAIL;
import static com.tadah.user.UserConstants.PASSWORD;
import static com.tadah.user.domain.entities.UserTest.PASSWORD_ENCODER;
import static com.tadah.user.utils.JwtUtilTest.CLAIM_DATA;
import static com.tadah.user.utils.JwtUtilTest.INVALID_TOKEN;
import static com.tadah.user.utils.JwtUtilTest.JWT_UTIL;
import static com.tadah.user.utils.JwtUtilTest.VALID_TOKEN;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("AuthenticationService 클래스")
public class AuthenticationServiceTest {
    public static final String INVALID_CLAIMS_NAME_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJpbnZhbGlkIjoxfQ.QwqsY19u7hBbtd32x31vUX0L6wONcPv9Msh2wlanPoI";
    private static final User USER = mock(User.class);

    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;

    public AuthenticationServiceTest() {
        this.userRepository = mock(UserRepository.class);
        this.authenticationService = new AuthenticationService(JWT_UTIL, userRepository, PASSWORD_ENCODER);
    }

    @Nested
    @DisplayName("publishToken 메서드는")
    public final class Describe_publishToken {
        private String subject(final String email, final String password) {
            return authenticationService.login(email, password);
        }

        @BeforeEach
        private void beforeEach() {
            when(userRepository.findByEmail(INVALID_EMAIL))
                .thenReturn(Optional.empty());
            when(userRepository.findByEmail(EMAIL))
                .thenReturn(Optional.of(USER));
            when(USER.getId())
                .thenReturn(CLAIM_DATA);
            when(USER.authenticate(PASSWORD, PASSWORD_ENCODER))
                .thenReturn(true);
        }

        @AfterEach
        private void afterEach() {
            verify(userRepository, atMostOnce())
                .findByEmail(anyString());
        }

        @Nested
        @DisplayName("이메일에 해당하는 사용자 정보가 없는경우")
        public final class Context_invalidEmail {
            @Test
            @DisplayName("LoginFailException을 던진다.")
            public void it_throws_login_fail_exception() {
                assertThatThrownBy(() -> subject(INVALID_EMAIL, PASSWORD))
                    .isInstanceOf(LoginFailException.class);
            }
        }

        @Nested
        @DisplayName("비밀번호가 일치하지 않은 경우")
        public final class Context_invalidPassword {
            @Test
            @DisplayName("LoginFailException을 던진다.")
            public void it_throws_login_fail_exception() {
                assertThatThrownBy(() -> subject(EMAIL, PASSWORD + "invalid"))
                    .isInstanceOf(LoginFailException.class);
            }
        }

        @Test
        @DisplayName("JWT를 발행한다.")
        public void it_publishes_json_web_token() {
            assertThat(subject(EMAIL, PASSWORD))
                .isEqualTo(VALID_TOKEN);
        }
    }

    @Nested
    @DisplayName("verifyToken 메서드는")
    public final class Describe_verifyToken {
        private User subject(final String token) {
            return authenticationService.verifyToken(token);
        }

        @BeforeEach
        private void beforeEach() {
            when(userRepository.findById(CLAIM_DATA))
                .thenReturn(Optional.of(USER));
        }

        @Nested
        @DisplayName("토큰이 유효하지 않은 경우")
        public final class Context_invalidToken {
            @Test
            @DisplayName("InvalidTokenException을 던진다.")
            public void it_throws_invalid_token_exception() {
                assertThatThrownBy(() -> subject(INVALID_TOKEN))
                    .isInstanceOf(InvalidTokenException.class);
            }
        }

        @Nested
        @DisplayName("속성 정보를 찾을 수 없는 경우")
        public final class Context_emptyClaims {
            @Test
            @DisplayName("InvalidTokenException을 던진다.")
            public void it_throws_invalid_token_exception() {
                assertThatThrownBy(() -> subject(INVALID_CLAIMS_NAME_TOKEN))
                    .isInstanceOf(InvalidTokenException.class);
            }
        }

        @Nested
        @DisplayName("토큰에 해당하는 사용자를 찾을 수 없는 경우")
        public final class Context_userNotFound {
            @BeforeEach
            private void beforeEach() {
                when(userRepository.findById(CLAIM_DATA))
                    .thenReturn(Optional.empty());
            }

            @Test
            @DisplayName("InvalidTokenException을 던진다.")
            public void it_throws_invalid_token_exception() {
                assertThatThrownBy(() -> subject(VALID_TOKEN))
                    .isInstanceOf(InvalidTokenException.class);
            }
        }

        @Test
        @DisplayName("사용자 정보를 리턴한다.")
        public void it_returns_a_user_data() {
            assertThat(subject(VALID_TOKEN))
                .isInstanceOf(User.class);
        }
    }
}
