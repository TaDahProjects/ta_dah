package com.tadah.auth.controllers;

import com.tadah.auth.exceptions.LoginFailException;
import com.tadah.common.dtos.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * Session 컨트롤러에서 던져진 예외를 처리한다.
 */
@RestControllerAdvice
public final class SessionControllerAdvice {
    /**
     * 로그인에 실패한 경우
     * 해당 예외가 어디서 던져졌는지 리턴한다.
     *
     * @param request 예외가 던져진 http 요청
     * @param exception 컨트롤러에서 던져진 예외
     * @return 던져진 예외의 내용 및 위치
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(LoginFailException.class)
    public ErrorResponse handleLoginFailException(
        final HttpServletRequest request, final LoginFailException exception
    ) {
        return new ErrorResponse(request, exception.getMessage());
    }
}
