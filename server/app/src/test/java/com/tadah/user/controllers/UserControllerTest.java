package com.tadah.user.controllers;

import com.tadah.common.dtos.ErrorResponse;
import com.tadah.user.applications.UserService;
import com.tadah.user.domain.UserType;
import com.tadah.user.domain.entities.User;
import com.tadah.user.dto.RegisterUserData;
import com.tadah.user.exceptions.UserEmailAlreadyExistException;
import com.tadah.utils.Parser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.stream.Stream;

import static com.tadah.user.UserConstants.CREATE_USER_URL;
import static com.tadah.user.UserConstants.EMAIL;
import static com.tadah.user.UserConstants.INVALID_EMAIL;
import static com.tadah.user.UserConstants.INVALID_PASSWORD_LOWER_CASE;
import static com.tadah.user.UserConstants.INVALID_PASSWORD_NUMBER;
import static com.tadah.user.UserConstants.INVALID_PASSWORD_SPECIAL_CASE;
import static com.tadah.user.UserConstants.INVALID_PASSWORD_UPPER_CASE;
import static com.tadah.user.UserConstants.NAME;
import static com.tadah.user.UserConstants.PASSWORD;
import static com.tadah.user.UserConstants.RIDER;
import static com.tadah.user.UserConstants.REGISTER_USER_DATA_RIDER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("UserController 클래스")
@WebMvcTest(UserController.class)
public final class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @BeforeEach
    private void beforeEach() {
        reset(userService);
    }

    @Nested
    @DisplayName("createUser 메서드는")
    public final class Describe_createUser {
        private ResultActions subject(final String requestBody, final ResultMatcher responseStatus) throws Exception {
            return mockMvc.perform(
                post(CREATE_USER_URL)
                    .accept(MediaType.APPLICATION_JSON_UTF8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
            ).andExpect(responseStatus);
        }

        @BeforeEach
        public void beforeEach() {
            when(userService.registerUser(any(User.class)))
                .thenReturn(RIDER);
        }

        @AfterEach
        public void afterEach() {
            verify(userService, atMostOnce())
                .registerUser(any(User.class));
        }

        @Test
        @DisplayName("User를 생성한다.")
        public void it_creates_the_user() throws Exception {
            subject(Parser.toJson(REGISTER_USER_DATA_RIDER), status().isCreated())
                .andExpect(
                    content().string(
                        Parser.toJson(RIDER)
                    ));
        }

        @Nested
        @TestInstance(Lifecycle.PER_CLASS)
        @DisplayName("유효하지 않은 데이터를 입력한 경우")
        public final class Context_invalidData {
            private Stream<Arguments> methodSource() throws Exception {
                return Stream.of(
                    Arguments.of(Parser.toJson(new RegisterUserData(null, NAME, PASSWORD, UserType.DRIVER)), "이메일이 입력되지 않았습니다."),
                    Arguments.of(Parser.toJson(new RegisterUserData(null, NAME, PASSWORD, UserType.RIDER)), "이메일이 입력되지 않았습니다."),
                    Arguments.of(Parser.toJson(new RegisterUserData("", NAME, PASSWORD, UserType.DRIVER)), "유효하지 않은 이메일 형식입니다."),
                    Arguments.of(Parser.toJson(new RegisterUserData("", NAME, PASSWORD, UserType.RIDER)), "유효하지 않은 이메일 형식입니다."),
                    Arguments.of(Parser.toJson(new RegisterUserData(INVALID_EMAIL, NAME, PASSWORD, UserType.DRIVER)), "유효하지 않은 이메일 형식입니다."),
                    Arguments.of(Parser.toJson(new RegisterUserData(INVALID_EMAIL, NAME, PASSWORD, UserType.RIDER)), "유효하지 않은 이메일 형식입니다."),
                    Arguments.of(Parser.toJson(new RegisterUserData(EMAIL, null, PASSWORD, UserType.DRIVER)), "이름이 입력되지 않았습니다."),
                    Arguments.of(Parser.toJson(new RegisterUserData(EMAIL, "", PASSWORD, UserType.DRIVER)), "이름이 입력되지 않았습니다."),
                    Arguments.of(Parser.toJson(new RegisterUserData(EMAIL, null, PASSWORD, UserType.RIDER)), "이름이 입력되지 않았습니다."),
                    Arguments.of(Parser.toJson(new RegisterUserData(EMAIL, "", PASSWORD, UserType.RIDER)), "이름이 입력되지 않았습니다."),
                    Arguments.of(Parser.toJson(new RegisterUserData(EMAIL, NAME, null, UserType.DRIVER)), "비밀번호가 입력되지 않았습니다."),
                    Arguments.of(Parser.toJson(new RegisterUserData(EMAIL, NAME, null, UserType.RIDER)), "비밀번호가 입력되지 않았습니다."),
                    Arguments.of(Parser.toJson(new RegisterUserData(EMAIL, NAME, "", UserType.DRIVER)), "최소 한개 이상의 대소문자와 숫자, 특수문자를 포함한 8자 이상의 비밀번호를 입력해야합니다."),
                    Arguments.of(Parser.toJson(new RegisterUserData(EMAIL, NAME, "", UserType.RIDER)), "최소 한개 이상의 대소문자와 숫자, 특수문자를 포함한 8자 이상의 비밀번호를 입력해야합니다."),
                    Arguments.of(Parser.toJson(new RegisterUserData(EMAIL, NAME, INVALID_PASSWORD_LOWER_CASE, UserType.DRIVER)), "최소 한개 이상의 대소문자와 숫자, 특수문자를 포함한 8자 이상의 비밀번호를 입력해야합니다."),
                    Arguments.of(Parser.toJson(new RegisterUserData(EMAIL, NAME, INVALID_PASSWORD_LOWER_CASE, UserType.RIDER)), "최소 한개 이상의 대소문자와 숫자, 특수문자를 포함한 8자 이상의 비밀번호를 입력해야합니다."),
                    Arguments.of(Parser.toJson(new RegisterUserData(EMAIL, NAME, INVALID_PASSWORD_UPPER_CASE, UserType.DRIVER)), "최소 한개 이상의 대소문자와 숫자, 특수문자를 포함한 8자 이상의 비밀번호를 입력해야합니다."),
                    Arguments.of(Parser.toJson(new RegisterUserData(EMAIL, NAME, INVALID_PASSWORD_UPPER_CASE, UserType.RIDER)), "최소 한개 이상의 대소문자와 숫자, 특수문자를 포함한 8자 이상의 비밀번호를 입력해야합니다."),
                    Arguments.of(Parser.toJson(new RegisterUserData(EMAIL, NAME, INVALID_PASSWORD_NUMBER, UserType.DRIVER)), "최소 한개 이상의 대소문자와 숫자, 특수문자를 포함한 8자 이상의 비밀번호를 입력해야합니다."),
                    Arguments.of(Parser.toJson(new RegisterUserData(EMAIL, NAME, INVALID_PASSWORD_NUMBER, UserType.RIDER)), "최소 한개 이상의 대소문자와 숫자, 특수문자를 포함한 8자 이상의 비밀번호를 입력해야합니다."),
                    Arguments.of(Parser.toJson(new RegisterUserData(EMAIL, NAME, INVALID_PASSWORD_SPECIAL_CASE, UserType.DRIVER)), "최소 한개 이상의 대소문자와 숫자, 특수문자를 포함한 8자 이상의 비밀번호를 입력해야합니다."),
                    Arguments.of(Parser.toJson(new RegisterUserData(EMAIL, NAME, INVALID_PASSWORD_SPECIAL_CASE, UserType.RIDER)), "최소 한개 이상의 대소문자와 숫자, 특수문자를 포함한 8자 이상의 비밀번호를 입력해야합니다."),
                    Arguments.of(Parser.toJson(new RegisterUserData(EMAIL, NAME, PASSWORD, null)), "사용자 타입이 입력되지 않았습니다."),
                    Arguments.of("{\"email\":\"test@test.com\",\"name\":\"name\",\"password\":\"password\",\"userType\":\"INVALID\"}", "유효하지 않은 입력형식입니다."),
                    Arguments.of("{\"email\":1,\"name\":\"name\",\"password\":\"password\",\"userType\":\"INVALID\"}", "유효하지 않은 입력형식입니다."),
                    Arguments.of("{\"email\":\"test@test.com\",\"name\":2,\"password\":\"password\",\"userType\":\"INVALID\"}", "유효하지 않은 입력형식입니다."),
                    Arguments.of("{\"email\":\"test@test.com\",\"name\":\"name\",\"password\":3,\"userType\":\"INVALID\"}", "유효하지 않은 입력형식입니다.")
                );
            }

            @MethodSource("methodSource")
            @DisplayName("입력 데이터가 잘못되었음을 알려준다.")
            @ParameterizedTest(name = "input=\"{0}\" errorMessage=\"{1}\"")
            public void it_notifies_that_input_data_is_invalid(final String input, final String errorMessage) throws Exception {
                subject(input, status().isBadRequest())
                    .andExpect(content().string(
                        Parser.toJson(
                            new ErrorResponse(CREATE_USER_URL, HttpMethod.POST.toString(), errorMessage)
                        )
                    ));
            }
        }

        @Nested
        @DisplayName("등록하려는 사용자의 이메일이 이미 존재하는 경우")
        public final class Context_emailAlreadyExists {
            @BeforeEach
            private void beforeEach() {
                when(userService.registerUser(any(User.class)))
                    .thenThrow(new UserEmailAlreadyExistException());
            }

            @Test
            @DisplayName("이메일이 이미 존재함을 알려준다.")
            public void it_notifies_that_email_already_exists() throws Exception {
                subject(Parser.toJson(REGISTER_USER_DATA_RIDER), status().isBadRequest())
                    .andExpect(
                        content().string(
                            Parser.toJson(
                                new ErrorResponse(
                                    CREATE_USER_URL, HttpMethod.POST.toString(),
                                    new UserEmailAlreadyExistException().getMessage()
                                ))));
            }
        }
    }
}
