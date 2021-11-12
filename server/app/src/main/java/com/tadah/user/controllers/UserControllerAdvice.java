package com.tadah.user.controllers;

import com.tadah.common.dtos.ErrorResponse;
import com.tadah.user.exceptions.UserEmailAlreadyExistException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * User 컨트롤러에서 던져진 예외를 처리한다.
 */
@RestControllerAdvice
public final class UserControllerAdvice {
    /**
     * 등록하려는 이메일이 이미 존재하는 경우
     * 해당 예외가 어디서 던져졌는지 리턴한다.
     *
     * @param request 예외가 던져진 http 요청
     * @param exception 컨트롤레에서 던져진 예외
     * @return 던져진 예외의 내용 및 위치
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserEmailAlreadyExistException.class)
    public ErrorResponse handleUserEmailAlreadyExistException(
        final HttpServletRequest request, final UserEmailAlreadyExistException exception
    ) {
        return new ErrorResponse(request, exception.getMessage());
    }
}
