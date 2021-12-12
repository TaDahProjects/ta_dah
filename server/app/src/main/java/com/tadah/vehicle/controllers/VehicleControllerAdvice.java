package com.tadah.vehicle.controllers;

import com.tadah.common.dtos.ErrorResponse;
import com.tadah.vehicle.exceptions.VehicleAlreadyExistException;
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
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(VehicleAlreadyExistException.class)
    public ErrorResponse handleVehicleAlreadyExistException(
        final HttpServletRequest request, final VehicleAlreadyExistException exception
    ) {
        return new ErrorResponse(request, exception.getMessage());
    }
}
