package com.tadah.user.dto;

import com.tadah.user.domain.entities.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.tadah.user.UserConstants.REGISTER_USER_DATA_DRIVER;
import static com.tadah.user.UserConstants.REGISTER_USER_DATA_RIDER;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("RegisterUserData 클래스")
public class RegisterUserDataTest {
    @Nested
    @DisplayName("toEntity 메서드는")
    public final class Describe_toEntity {
        private User subject(final RegisterUserData userData) {
            return userData.toEntity();
        }

        @Test
        @DisplayName("DTO를 User로 변환한다.")
        public void it_converts_the_dto_to_the_user() {
            assertThat(subject(REGISTER_USER_DATA_DRIVER))
                .isNotNull()
                .isInstanceOf(User.class)
                .matches(user -> REGISTER_USER_DATA_DRIVER.getEmail().equals(user.getEmail()))
                .matches(user -> REGISTER_USER_DATA_DRIVER.getName().equals(user.getName()))
                .matches(user -> REGISTER_USER_DATA_DRIVER.getPassword().equals(user.getPassword()))
                .matches(user -> REGISTER_USER_DATA_DRIVER.getUserType().equals(user.getUserType()));
            assertThat(subject(REGISTER_USER_DATA_RIDER))
                .isNotNull()
                .isInstanceOf(User.class)
                .matches(user -> REGISTER_USER_DATA_RIDER.getEmail().equals(user.getEmail()))
                .matches(user -> REGISTER_USER_DATA_RIDER.getName().equals(user.getName()))
                .matches(user -> REGISTER_USER_DATA_RIDER.getPassword().equals(user.getPassword()))
                .matches(user -> REGISTER_USER_DATA_RIDER.getUserType().equals(user.getUserType()));
        }
    }
}
