package com.tadah.user.controllers;

import com.tadah.user.applications.UserService;
import com.tadah.user.domain.entities.User;
import com.tadah.user.dto.UserData;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * User 관련 요청을 담당한다.
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
     * User를 생성하고 리턴한다.
     *
     * @param userData 생성할 User 데이터
     * @return 생성한 User
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody @Valid final UserData userData) {
        final User user = userData.toEntity();
        return userService.saveUser(user);
    }
}
