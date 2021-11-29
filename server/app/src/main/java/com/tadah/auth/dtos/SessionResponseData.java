package com.tadah.auth.dtos;

import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;

/**
 * 인증 응답 데이터를 저장한다.
 */
@Generated
@Getter
@AllArgsConstructor
public final class SessionResponseData {
    private final String token;
}
