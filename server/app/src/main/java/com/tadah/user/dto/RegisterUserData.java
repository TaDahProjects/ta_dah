package com.tadah.user.dto;

import com.tadah.user.domain.UserType;
import com.tadah.user.domain.entities.User;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Generated
@Getter
public final class RegisterUserData extends UserData {
    /**
     * 비밀번호
     * null 여부 확인
     * 최소 한개 이상의 대소문자, 숫자, 특수문자 포함여부 및 길이 확인
     * 빈 문자열이 들어오는 경우 @Pattern에서 확인하므로 @NotBlank 대신 @NotNull 사용하였음
     */
    @NotNull(message = "비밀번호가 입력되지 않았습니다.")
    @Pattern(
        message = "최소 한개 이상의 대소문자와 숫자, 특수문자를 포함한 8자 이상의 비밀번호를 입력해야합니다.",
        regexp = "(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,100}"
    )
    private String password;

    public RegisterUserData(final String email, final String name, final String password, final UserType userType) {
        super(email, name, userType);
        this.password = password;
    }

    public User toEntity() {
        return new User(getEmail(), getName(), getUserType());
    }
}
