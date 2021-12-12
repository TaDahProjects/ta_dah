package com.tadah.auth.applications;

import com.tadah.auth.exceptions.InvalidTokenException;
import com.tadah.auth.exceptions.LoginFailException;
import com.tadah.user.domains.entities.User;
import com.tadah.user.domains.repositories.UserRepository;
import com.tadah.user.domains.repositories.infra.JpaUserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.tadah.auth.utils.JwtUtilTest.JWT_UTIL;
import static com.tadah.auth.utils.JwtUtilTest.VALID_TOKEN_INVALID_CLAIMS_NAME;
import static com.tadah.auth.utils.JwtUtilTest.INVALID_TOKEN;
import static com.tadah.auth.utils.JwtUtilTest.VALID_TOKEN;
import static com.tadah.user.domains.entities.UserTest.EMAIL;
import static com.tadah.user.domains.entities.UserTest.NAME;
import static com.tadah.user.domains.entities.UserTest.PASSWORD;
import static com.tadah.user.domains.entities.UserTest.USER;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DataJpaTest
@DisplayName("AuthenticationService 클래스")
public class AuthenticationServiceTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JpaUserRepository jpaUserRepository;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationService authenticationService;
    public AuthenticationServiceTest(
        @Autowired final UserRepository userRepository,
        @Autowired final PasswordEncoder passwordEncoder
    ) {
        this.passwordEncoder = passwordEncoder;
        this.authenticationService = new AuthenticationService(JWT_UTIL, userRepository, passwordEncoder);
    }

    private Long userId;
    @BeforeEach
    private void beforeEach() {
        USER.setPassword(PASSWORD, passwordEncoder);
        userId = userRepository.save(USER).getId();
    }

    @AfterEach
    private void afterEach() {
        jpaUserRepository.deleteAll();
    }

    private String getToken(final Long userId) {
        return JWT_UTIL.encode(userId);
    }

    @Nested
    @DisplayName("publish 메서드는")
    public final class Describe_publish {
        private String subject(final String email, final String password) {
            return authenticationService.publish(email, password);
        }

        @Nested
        @DisplayName("이메일에 해당하는 사용자 정보가 없는경우")
        public final class Context_invalidEmail {
            @BeforeEach
            private void beforeEach() {
                jpaUserRepository.deleteAll();
            }

            @Test
            @DisplayName("LoginFailException을 던진다.")
            public void it_throws_login_fail_exception() {
                assertThatThrownBy(() -> subject(EMAIL, PASSWORD))
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
                .isEqualTo(getToken(userId));
        }
    }

    @Nested
    @DisplayName("verify 메서드는")
    public final class Describe_verify {
        private User subject(final String token) {
            return authenticationService.verify(token);
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
                assertThatThrownBy(() -> subject(VALID_TOKEN_INVALID_CLAIMS_NAME))
                    .isInstanceOf(InvalidTokenException.class);
            }
        }

        @Nested
        @DisplayName("토큰에 해당하는 사용자를 찾을 수 없는 경우")
        public final class Context_userNotFound {
            @BeforeEach
            private void beforeEach() {
                jpaUserRepository.deleteAll();
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
            assertThat(subject(getToken(userId)))
                .isInstanceOf(User.class)
                .matches(user -> EMAIL.equals(user.getEmail()))
                .matches(user -> NAME.equals(user.getName()))
                .matches(user -> passwordEncoder.matches(PASSWORD, user.getPassword()));
        }
    }
}
