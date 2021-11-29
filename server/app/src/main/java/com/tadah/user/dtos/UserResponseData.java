package com.tadah.user.dtos;

import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;

/**
 * 사용자 응답 데이터를 저장한다.
 */
@Generated
@Getter
@AllArgsConstructor
public class UserResponseData {
    private final String email;
    private final String name;
    private final String userType;
}
