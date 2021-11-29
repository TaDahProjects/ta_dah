package com.tadah.user.exceptions;

/**
 * 사용자가 등록하려는 이메일이 이미 존재하는 경우 던져진다.
 */
public final class UserEmailAlreadyExistException extends RuntimeException {
    public UserEmailAlreadyExistException() {
        super("이미 존재하는 사용자 이메일입니다.");
    }
}
