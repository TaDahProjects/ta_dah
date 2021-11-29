package com.tadah.user;

import com.tadah.user.domain.UserType;
import com.tadah.user.domain.entities.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 테스트에 반복적으로 사용되는 상수값 정의
 */
public interface UserConstants {
    String EMAIL = "test@test.com";
    String INVALID_EMAIL = "test@testcom";
    String NAME = "name";
    String PASSWORD = "Password123!!";
    String ENCODED_PASSWORD = "Password123!!" + "Encoded";
    String INVALID_PASSWORD_UPPER_CASE = "password123!!";
    String INVALID_PASSWORD_LOWER_CASE = "PASSWORD123!!";
    String INVALID_PASSWORD_NUMBER = "Password!!";
    String INVALID_PASSWORD_SPECIAL_CASE = "Password123";
    User DRIVER = setUserWithPassword(UserType.DRIVER);
    User RIDER = setUserWithPassword(UserType.RIDER);
    User RIDER_WITH_OUT_PASSWORD = setUser(UserType.RIDER);
    User DRIVER_WITH_OUT_PASSWORD = setUser(UserType.DRIVER);

    private static User setUser(final UserType userType) {
        return new User(EMAIL, NAME, userType);
    }
    private static User setUserWithPassword(final UserType userType) {
        final User user = setUser(userType);
        user.updatePassword(PASSWORD, new BCryptPasswordEncoder());
        return user;
    }
}
