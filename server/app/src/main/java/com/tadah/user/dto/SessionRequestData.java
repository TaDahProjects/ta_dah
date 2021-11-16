package com.tadah.user.dto;

import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Generated
@Getter
@AllArgsConstructor
public final class SessionRequestData {
    /**
     * 이메일
     * RFC-5322 기반 이메일 형식 확인 및 null 여부 확인
     * 빈 문자열이 들어오는 경우 @Email에서 확인하므로 @NotBlank 대신 @NotNull 사용하였음
     *
     * @see <a href="https://www.ietf.org/rfc/rfc5322.txt">RFC-5322</a>, <a herf="https://emailregex.com">이메일 정규 표현식</a>
     */
    @NotNull(message = "이메일이 입력되지 않았습니다.")
    @Email(
        message = "유효하지 않은 이메일 형식입니다.",
        regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"
    )
    private String email;

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
}
