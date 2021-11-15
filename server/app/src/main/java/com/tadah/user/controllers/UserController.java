package com.tadah.user.controllers;

import com.tadah.user.applications.UserService;
import com.tadah.user.domain.entities.User;
import com.tadah.user.dto.RegisterUserData;
import com.tadah.user.exceptions.UserEmailAlreadyExistException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 사용자 등록을 수행한다.
 */
@CrossOrigin
@RestController
@RequestMapping("/users")
public final class UserController {
    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    /**
     * 사용자 등록을 수행한다.
     *
     * @param registerUserData 등록할 사용자 정보
     * @return 등록한 사용자 정보
     * @throws UserEmailAlreadyExistException 이미 존재하는 사용자 이메일인 경우
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User register(@RequestBody @Valid final RegisterUserData registerUserData) {
        final User user = registerUserData.toEntity();
        return userService.registerUser(user, registerUserData.getPassword());
    }
}
