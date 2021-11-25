package com.tadah.auth.exceptions;

/**
 * JWT 검증에 실패한경우 던져진다.
 */
public final class InvalidTokenException extends RuntimeException {
    public InvalidTokenException() {
        super("유효하지 않은 토큰입니다.");
    }
}
