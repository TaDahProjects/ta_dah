package com.tadah.common.controllers;

import com.tadah.common.dtos.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 컨트롤러에서 던져진 예외를 처리한다.
 */
@RestControllerAdvice
public final class ControllerAdvice {
    /**
     * 입력 형식에 맞지 않는 경우
     * 해당 예외가 어디서 던져졌는지 리턴한다.
     *
     * @param request 예외가 던져진 http 요청
     * @param exception 컨트롤러에서 던져진 예외
     * @return 던져진 예외의 내용 및 위치
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleInvalidInputData(
        final HttpServletRequest request, final MethodArgumentNotValidException exception
    ) {
        return new ErrorResponse(
            request,
            exception.getBindingResult()
                .getAllErrors()
                .get(0)
                .getDefaultMessage()
        );
    }

    /**
     * 파싱에 실패한 경우
     * 해당 예외가 어디서 던져졌는지 리턴한다.
     *
     * @param request 예외가 던져진 http 요청
     * @return 던져진 예외의 내용 및 위치
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ErrorResponse handleParsingException(final HttpServletRequest request) {
        return new ErrorResponse(request, "유효하지 않은 입력형식입니다.");
    }
}
