package com.tadah.user.domain.entities;

import com.tadah.user.domain.UserType;
import lombok.AccessLevel;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Generated
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class User {
    @Id
    @GeneratedValue
    private Long id;

    private String email;

    private String name;

    private String password;

    private UserType userType;

    public User(final String email, final String name, final String password, final  UserType userType) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.userType = userType;
    }
}
