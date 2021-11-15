package com.tadah.user.dto;

import com.tadah.user.domain.UserType;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Generated
@Getter
@AllArgsConstructor
public class UserData {
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

    @NotBlank(message = "이름이 입력되지 않았습니다.")
    private String name;

    /**
     * 사용자 타입
     * null 또는 UserType을 제외한 값이 들어오는 경우 json 파싱 단계에서 먼저 에러가 발생함
     * 따라서 null 또는 UserType을 제외한 값이 들어오는 경우에 관한 validator를 추가할 필요가 없음
     */
    @NotNull(message = "사용자 타입이 입력되지 않았습니다.")
    private UserType userType;
}
