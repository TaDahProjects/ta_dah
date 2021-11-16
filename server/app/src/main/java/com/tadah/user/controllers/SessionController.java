package com.tadah.user.controllers;

import com.tadah.user.applications.AuthenticationService;
import com.tadah.user.dto.SessionRequestData;
import com.tadah.user.dto.SessionResponseData;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.tadah.user.exceptions.LoginFailException;

import javax.validation.Valid;

/**
 * 로그인을 담당한다.
 */
@CrossOrigin
@RestController
@RequestMapping("/session")
public final class SessionController {
    private final AuthenticationService authenticationService;

    public SessionController(final AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * JWT 발행을 담당한다.
     *
     * @param requestData 이메일, 비밀번호
     * @return JWT
     * @throws LoginFailException 이메일 또는 비밀번호가 유효하지 않은 경우
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SessionResponseData login(@RequestBody @Valid final SessionRequestData requestData) {
        final String token = authenticationService.login(requestData.getEmail(), requestData.getPassword());
        return new SessionResponseData(token);
    }
}