package com.tadah.user.applications;

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
import static com.tadah.user.UserConstants.RIDER;
import static com.tadah.user.domain.entities.UserTest.PASSWORD_ENCODER;
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
                .thenReturn(Optional.of(RIDER));
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
}
