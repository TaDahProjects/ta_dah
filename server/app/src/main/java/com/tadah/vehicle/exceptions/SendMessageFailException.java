package com.tadah.vehicle.exceptions;

/**
 * 메시지 전송 실패한 경우 던져진다.
 */
public class SendMessageFailException extends RuntimeException {
    public SendMessageFailException() {
        super("메시지 전송에 실패했습니다.");
    }
}
