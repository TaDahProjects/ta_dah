package com.tadah.user.domain.entities;

import com.tadah.user.domain.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.tadah.user.UserConstants.EMAIL;
import static com.tadah.user.UserConstants.INVALID_PASSWORD_LOWER_CASE;
import static com.tadah.user.UserConstants.INVALID_PASSWORD_NUMBER;
import static com.tadah.user.UserConstants.INVALID_PASSWORD_SPECIAL_CASE;
import static com.tadah.user.UserConstants.INVALID_PASSWORD_UPPER_CASE;
import static com.tadah.user.UserConstants.NAME;
import static com.tadah.user.UserConstants.PASSWORD;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("User 클래스")
public final class UserTest {
    public static final Long USER_ID = 1L;
    public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    private User user;
    @BeforeEach
    private void beforeEach() {
        user = new User(EMAIL, NAME, UserType.RIDER);
    }

    @Nested
    @DisplayName("updatePassword 메서드는")
    public final class Describe_updatePassword {
        private void subject() {
            user.updatePassword(PASSWORD, PASSWORD_ENCODER);
        }

        @Test
        @DisplayName("비밀번호를 암호화하여 저장한다.")
        public void it_encrypts_the_password_and_saves() {
            subject();

            assertThat(PASSWORD_ENCODER.matches(PASSWORD, user.getPassword()))
                .isTrue();
        }
    }

    @Nested
    @DisplayName("authenticate 메서드는")
    public final class Describe_authenticate {
        @BeforeEach
        private void beforeEach() {
            new Describe_updatePassword().subject();
        }

        private boolean subject(final String password) {
            return user.authenticate(password, PASSWORD_ENCODER);
        }

        @Test
        @DisplayName("비밀번호를 인증한다.")
        public void it_authenticates_the_password() {
            assertThat(subject(PASSWORD))
                .isTrue();
        }

        @Nested
        @DisplayName("비밀번호가 일치하지 않는 경우")
        public final class Context_invalidPassword {
            @Test
            @DisplayName("인증에 실패하였음을 알려준다.")
            public void it_notifies_that_user_authentication_failed() {
                assertThat(subject(INVALID_PASSWORD_NUMBER))
                    .isFalse();

                assertThat(subject(INVALID_PASSWORD_SPECIAL_CASE))
                    .isFalse();

                assertThat(subject(INVALID_PASSWORD_UPPER_CASE))
                    .isFalse();

                assertThat(subject(INVALID_PASSWORD_LOWER_CASE))
                    .isFalse();
            }
        }
    }
}
