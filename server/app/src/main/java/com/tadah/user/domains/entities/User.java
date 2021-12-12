package com.tadah.user.domains.entities;

import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Generated
@Entity
@Getter
@NoArgsConstructor
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

    /**
     * 비밀번호를 설정한다.
     *
     * @param password 설정할 비밀번호
     * @param passwordEncoder 비밀번호를 암호화할 인코더
     */
    public void setPassword(final String password, final PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
    }

    /**
     * 사용자의 비밀번호를 검증한다.
     *
     * @param password 검증할 비밀번호
     * @param passwordEncoder 검증에 사용할 인코더
     * @return 검증 결과
     */
    public boolean verifyPassword(final String password, final PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(password, this.password);
    }
}
