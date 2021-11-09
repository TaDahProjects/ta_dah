package com.tadah.user.dto;

import com.tadah.user.domain.entities.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.tadah.user.UserConstants.DRIVER_USER_DATA;
import static com.tadah.user.UserConstants.RIDER_USER_DATA;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("UserData 클래스")
public class UserDataTest {
    @Nested
    @DisplayName("toEntity 메서드는")
    public final class Describe_toEntity {
        private User subject(final UserData userData) {
            return userData.toEntity();
        }

        @Test
        @DisplayName("UserData를 User로 변환한다.")
        public void it_converts_the_user_data_to_the_user() {
            assertThat(subject(DRIVER_USER_DATA))
                .isNotNull()
                .isInstanceOf(User.class)
                .matches(user -> DRIVER_USER_DATA.getEmail().equals(user.getEmail()))
                .matches(user -> DRIVER_USER_DATA.getName().equals(user.getName()))
                .matches(user -> DRIVER_USER_DATA.getPassword().equals(user.getPassword()))
                .matches(user -> DRIVER_USER_DATA.getUserType().equals(user.getUserType()));
            assertThat(subject(RIDER_USER_DATA))
                .isNotNull()
                .isInstanceOf(User.class)
                .matches(user -> RIDER_USER_DATA.getEmail().equals(user.getEmail()))
                .matches(user -> RIDER_USER_DATA.getName().equals(user.getName()))
                .matches(user -> RIDER_USER_DATA.getPassword().equals(user.getPassword()))
                .matches(user -> RIDER_USER_DATA.getUserType().equals(user.getUserType()));
        }
    }
}
