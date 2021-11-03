package com.tadah.user.domain.entities;

import com.tadah.user.domain.UserType;
import lombok.Generated;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Generated
@Entity
@Getter
public final class User {
    @Id
    @GeneratedValue
    private Long id;

    private String email;

    private String name;

    private String password;

    private UserType userType;
}
