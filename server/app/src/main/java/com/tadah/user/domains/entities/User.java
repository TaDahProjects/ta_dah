package com.tadah.user.domains.entities;

import com.tadah.user.domains.UserType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Generated
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public final class User {
    @Id
    @GeneratedValue
    private Long id;

    private String email;

    private String name;

    @NotNull
    private String password;

    public User(final String email, final String name) {
        this.email = email;
        this.name = name;
    }

    public void updatePassword(final String password, final PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
    }

    public boolean authenticate(final String password, final PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(password, this.password);
    }
}
