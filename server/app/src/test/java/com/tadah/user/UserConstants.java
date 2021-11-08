package com.tadah.user;

import com.tadah.user.domain.UserType;
import com.tadah.user.domain.entities.User;

/**
 * 테스트에 반복적으로 사용되는 상수값 정의
 */
public interface UserConstants {
    String EMAIL = "test@test.com";
    String NAME = "name";
    String PASSWORD = "password";
    User DRIVER = new User(EMAIL, NAME, PASSWORD, UserType.DRIER);
    User RIDER = new User(EMAIL, NAME, PASSWORD, UserType.RIDER);
}
