package com.tadah.user.applications;

import com.tadah.user.domain.entities.User;
import com.tadah.user.domain.repositories.UserRepository;
import com.tadah.user.exceptions.UserEmailAlreadyExistException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.tadah.user.UserConstants.DRIVER;
import static com.tadah.user.UserConstants.EMAIL;
import static com.tadah.user.UserConstants.ENCODED_PASSWORD;
import static com.tadah.user.UserConstants.PASSWORD;
import static com.tadah.user.UserConstants.RIDER;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("UserService 클래스")
@ExtendWith(MockitoExtension.class)
public final class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

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
                when(passwordEncoder.encode(anyString()))
                    .thenReturn(ENCODED_PASSWORD);
                when(userRepository.save(any(User.class)))
                    .thenReturn(DRIVER);
            }

            @AfterEach
            private void afterEach() {
                verify(passwordEncoder, atMostOnce())
                    .encode(anyString());
                verify(userRepository, atMostOnce())
                    .save(any(User.class));
            }

            @Test
            @DisplayName("사용자를 등록한다.")
            public void it_registers_users() {
                assertThat(subject(DRIVER))
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
                verify(passwordEncoder, times(0))
                    .encode(anyString());
                verify(userRepository, times(0))
                    .save(any(User.class));
            }

            @Test
            @DisplayName("UserEmailDuplicationException을 던진다.")
            public void it_throws_user_email_duplication_exception() {
                assertThatThrownBy(() -> subject(RIDER))
                    .isInstanceOf(UserEmailAlreadyExistException.class);
            }
        }
    }
}
