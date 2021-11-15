package com.tadah.user.domain.entities;

import com.tadah.user.domain.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.tadah.user.UserConstants.EMAIL;
import static com.tadah.user.UserConstants.NAME;
import static com.tadah.user.UserConstants.PASSWORD;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("User 클래스")
public final class UserTest {
    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

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
}
