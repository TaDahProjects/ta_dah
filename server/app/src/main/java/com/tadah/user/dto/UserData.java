package com.tadah.user.dto;

import com.tadah.user.domain.UserType;
import com.tadah.user.domain.entities.User;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;

@Generated
@Getter
@AllArgsConstructor
public final class UserData {
    private String email;

    private String name;

    private String password;

    private UserType userType;

    public User toEntity() {
        return new User(email, name, password, userType);
    }
}
