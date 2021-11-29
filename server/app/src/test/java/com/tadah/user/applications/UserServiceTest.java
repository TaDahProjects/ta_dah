package com.tadah.user.applications;

import com.tadah.user.domains.entities.User;
import com.tadah.user.domains.repositories.UserRepository;
import com.tadah.user.exceptions.UserEmailAlreadyExistException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.tadah.user.domains.entities.UserTest.EMAIL;
import static com.tadah.user.domains.entities.UserTest.PASSWORD;
import static com.tadah.user.domains.entities.UserTest.PASSWORD_ENCODER;
import static com.tadah.user.domains.entities.UserTest.USER;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("UserService 클래스")
public final class UserServiceTest {
    private final UserService userService;
    private final UserRepository userRepository;

    public UserServiceTest() {
        this.userRepository = mock(UserRepository.class);
        this.userService = new UserService(userRepository, PASSWORD_ENCODER);
    }

    @Nested
    @DisplayName("registerUser 메서드는")
    public final class Describe_registerUser {
        private User subject(final User user) {
            return userService.registerUser(user, PASSWORD);
        }

        @AfterEach
        private void afterEach() {
            verify(userRepository, atMostOnce())
                .existsByEmail(anyString());
        }

        @Nested
        @DisplayName("이메일에 해당하는 사용자가 없는경우")
        public final class Context_validEmail {
            @BeforeEach
            private void beforeEach() {
                when(userRepository.save(any(User.class)))
                    .thenReturn(USER);
            }

            @AfterEach
            private void afterEach() {
                verify(userRepository, atMostOnce())
                    .save(any(User.class));
            }

            @Test
            @DisplayName("사용자를 등록한다.")
            public void it_registers_users() {
                assertThat(subject(USER))
                    .isInstanceOf(User.class);
            }
        }

        @Nested
        @DisplayName("이메일에 해당하는 사용자가 있는 경우")
        public final class Context_invalidEmail {
            @BeforeEach
            private void beforeEach() {
                when(userRepository.existsByEmail(EMAIL))
                    .thenReturn(true);
            }

            @AfterEach
            private void afterEach() {
                verify(userRepository, times(0))
                    .save(any(User.class));
            }

            @Test
            @DisplayName("UserEmailDuplicationException을 던진다.")
            public void it_throws_user_email_duplication_exception() {
                assertThatThrownBy(() -> subject(USER))
                    .isInstanceOf(UserEmailAlreadyExistException.class);
            }
        }
    }
}
