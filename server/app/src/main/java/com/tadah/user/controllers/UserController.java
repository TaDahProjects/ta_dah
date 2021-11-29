package com.tadah.user.controllers;

import com.tadah.auth.applications.AuthorizationService;
import com.tadah.auth.domains.entities.Role;
import com.tadah.user.applications.UserService;
import com.tadah.user.domains.UserType;
import com.tadah.user.domains.entities.User;
import com.tadah.user.dtos.UserRequestData;
import com.tadah.user.dtos.UserResponseData;
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
    private final AuthorizationService authorizationService;

    public UserController(
        final UserService userService,
        final AuthorizationService authorizationService
    ) {
        this.userService = userService;
        this.authorizationService = authorizationService;
    }

    private User toUser(final UserRequestData userRequestData) {
        return new User(userRequestData.getEmail(), userRequestData.getName());
    }

    private Role toRole(final Long userId, final UserType userType) {
        return new Role(userId, userType.name());
    }

    private UserResponseData toUserResponseData(final User user, final Role role) {
        return new UserResponseData(user.getEmail(), user.getName(), role.getName());
    }

    /**
     * 사용자 등록을 수행한다.
     *
     * @param userRequestData 등록할 사용자 정보
     * @return 등록한 사용자 정보
     * @throws UserEmailAlreadyExistException 이미 존재하는 사용자 이메일인 경우
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseData register(@RequestBody @Valid final UserRequestData userRequestData) {
        final User user = userService.register(toUser(userRequestData), userRequestData.getPassword());
        final Role role = authorizationService.create(toRole(user.getId(), userRequestData.getUserType()));
        return toUserResponseData(user, role);
    }
}
