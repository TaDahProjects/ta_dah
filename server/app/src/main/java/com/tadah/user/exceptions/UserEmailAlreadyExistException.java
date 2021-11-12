package com.tadah.user.exceptions;

public final class UserEmailAlreadyExistException extends RuntimeException {
    public UserEmailAlreadyExistException() {
        super("이미 존재하는 사용자 이메일입니다.");
    }
}
