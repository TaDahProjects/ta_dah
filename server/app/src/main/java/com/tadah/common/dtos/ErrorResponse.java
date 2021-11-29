package com.tadah.common.dtos;

import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;

import javax.servlet.http.HttpServletRequest;

/**
 * 발생한 에러 정보를 저장한다.
 */
@Generated
@Getter
@AllArgsConstructor
public final class ErrorResponse {
    private final String url;
    private final String method;
    private final String errorMessage;

    public ErrorResponse(final HttpServletRequest request, final String errorMessage) {
        this.url = request.getRequestURI();
        this.method = request.getMethod();
        this.errorMessage = errorMessage;
    }
}
