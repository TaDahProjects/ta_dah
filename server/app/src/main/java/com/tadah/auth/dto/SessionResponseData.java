package com.tadah.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;

/**
 * 인증 결과를 저장한다.
 */
@Generated
@Getter
@AllArgsConstructor
public final class SessionResponseData {
    private final String token;
}
