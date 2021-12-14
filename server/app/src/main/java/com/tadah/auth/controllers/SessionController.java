package com.tadah.auth.controllers;

import com.tadah.auth.applications.AuthenticationService;
import com.tadah.auth.dtos.SessionRequestData;
import com.tadah.auth.dtos.SessionResponseData;
import com.tadah.user.domains.entities.User;
import com.tadah.user.dtos.UserResponseData;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.tadah.auth.exceptions.LoginFailException;

import javax.validation.Valid;

/**
 * 인증을 시작한다.
 */
@CrossOrigin
@RestController
@RequestMapping("/session")
public final class SessionController {
    private final AuthenticationService authenticationService;

    public SessionController(final AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    private SessionResponseData toDto(final String token) {
        return new SessionResponseData(token);
    }
    
    /**
     * JWT를 발행한다.
     *
     * @param requestData 이메일, 비밀번호
     * @return JWT
     * @throws LoginFailException 이메일 또는 비밀번호가 유효하지 않은 경우
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SessionResponseData login(@RequestBody @Valid final SessionRequestData requestData) {
        final String token = authenticationService.publish(requestData.getEmail(), requestData.getPassword());
        return toDto(token);
    }
}
