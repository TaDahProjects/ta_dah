package com.tadah.user.exceptions;

public final class UserEmailDuplicationException extends RuntimeException {
    public UserEmailDuplicationException() {
        super("이미 존재하는 사용자 이메일입니다.");
    }
}
