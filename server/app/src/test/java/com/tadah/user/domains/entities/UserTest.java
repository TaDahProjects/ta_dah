package com.tadah.user.domains.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("User 클래스")
public final class UserTest {
    public static final Long USER_ID = 1L;
    public static final String EMAIL = "test@test.com";
    public static final String NAME = "name";
    public static final String PASSWORD = "Password123!!";
    public static final User USER = new User(USER_ID, EMAIL, NAME, PASSWORD);

    public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    @Nested
    @DisplayName("updatePassword 메서드는")
    public final class Describe_updatePassword {
        private void subject() {
            USER.setPassword(PASSWORD, PASSWORD_ENCODER);
        }

        @BeforeEach
        private void beforeEach() {
            subject();
        }

        @Test
        @DisplayName("비밀번호를 암호화하여 저장한다.")
        public void it_encrypts_the_password_and_saves() {
            assertThat(PASSWORD_ENCODER.matches(PASSWORD, USER.getPassword()))
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
            return USER.verifyPassword(password, PASSWORD_ENCODER);
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
                assertThat(subject(PASSWORD + "invalid"))
                    .isFalse();
            }
        }
    }
}
