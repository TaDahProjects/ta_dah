package com.tadah.auth.exceptions;

/**
 * 로그인에 실패한경우 던져진다.
 */
public class LoginFailException extends RuntimeException {
    public LoginFailException() {
        super("로그인에 실패했습니다.");
    }
}
