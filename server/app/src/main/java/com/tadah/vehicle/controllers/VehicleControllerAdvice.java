package com.tadah.vehicle.controllers;

import com.tadah.common.dtos.ErrorResponse;
import com.tadah.vehicle.exceptions.VehicleAlreadyExistException;
import com.tadah.vehicle.exceptions.VehicleNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * Vehicle 컨트롤러에서 던져진 예외를 처리한다.
 */
@RestControllerAdvice
public final class VehicleControllerAdvice {
    /**
     * 사용자가 이미 차량을 등록한 경우
     * 해당 에외가 어디서 던져졌는지 리턴한다.
     *
     * @param request 예외가 던져진 http 요청
     * @param exception 컨트롤러에서 던져진 예외
     * @return 던져진 예외의 내용 및 위치
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(VehicleAlreadyExistException.class)
    public ErrorResponse handleVehicleAlreadyExistException(
        final HttpServletRequest request, final VehicleAlreadyExistException exception
    ) {
        return new ErrorResponse(request, exception.getMessage());
    }

    /**
     * 사용자가 소유한 차량을 찾을 수 없는 경우
     * 해당 예외가 어디서 던져졌는지 리턴한다.
     *
     * @param request 예외가 던져진 http 요청
     * @param exception 컨트롤러에서 던져진 예외
     * @return 던져진 예외의 내용 및 위치
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(VehicleNotFoundException.class)
    public ErrorResponse handleVehicleNotFoundException(
        final HttpServletRequest request, final VehicleNotFoundException exception
    ) {
        return new ErrorResponse(request, exception.getMessage());
    }
}
