package com.tadah.auth.domains.entities;

import org.junit.jupiter.api.DisplayName;

import static com.tadah.user.domains.entities.UserTest.USER_ID;

@DisplayName("Role 클래스")
public final class RoleTest {
    private static final Long ROLE_ID = 1L;
    private static final String ROLE_NAME = "role";
    public static final Role ROLE = new Role(ROLE_ID, USER_ID, ROLE_NAME);
}
