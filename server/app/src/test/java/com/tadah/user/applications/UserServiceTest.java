package com.tadah.user.applications;

import com.tadah.user.domain.entities.User;
import com.tadah.user.domain.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.tadah.user.UserConstants.DRIVER;
import static com.tadah.user.UserConstants.RIDER;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("UserService 클래스")
@ExtendWith(MockitoExtension.class)
public final class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Nested
    @DisplayName("saveUser 메서드는")
    public final class Context_saveUser {
        private User subject(final User user) {
            return userService.saveUser(user);
        }

        @BeforeEach
        private void beforeEach() {
            when(userRepository.save(DRIVER))
                .thenReturn(DRIVER);
            when(userRepository.save(RIDER))
                .thenReturn(RIDER);
        }

        @AfterEach
        private void afterEach() {
            verify(userRepository, atLeastOnce())
                .save(any(User.class));
        }

        @Test
        @DisplayName("User를 저장한다.")
        public void it_saves_the_user() {
            assertThat(subject(DRIVER))
                .isInstanceOf(User.class);
            assertThat(subject(RIDER))
                .isInstanceOf(User.class);
        }
    }
}
