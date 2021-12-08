package com.tadah.user.controllers;

import com.tadah.common.dtos.ErrorResponse;
import com.tadah.user.domains.repositories.UserRepository;
import com.tadah.user.domains.repositories.infra.JpaUserRepository;
import com.tadah.user.dtos.UserRequestData;
import com.tadah.user.dtos.UserResponseData;
import com.tadah.user.exceptions.UserEmailAlreadyExistException;
import com.tadah.utils.Parser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.stream.Stream;

import static com.tadah.user.domains.entities.UserTest.EMAIL;
import static com.tadah.user.domains.entities.UserTest.NAME;
import static com.tadah.user.domains.entities.UserTest.PASSWORD;
import static com.tadah.user.domains.entities.UserTest.getUser;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@ExtendWith(SpringExtension.class)
@DisplayName("UserController 클래스")
public final class UserControllerTest {
    public static final String INVALID_EMAIL = "test@testcom";
    public static final String INVALID_PASSWORD_UPPER_CASE = "password123!!";
    public static final String INVALID_PASSWORD_LOWER_CASE = "PASSWORD123!!";
    public static final String INVALID_PASSWORD_NUMBER = "Password!!";
    public static final String INVALID_PASSWORD_SPECIAL_CASE = "Password123";
    private static final String USERS_URL = "/users";

    private static String getUserRequest(final String email, final String name, final String password) throws Exception {
        return Parser.toJson(new UserRequestData(email, name, password));
    }

    private static String getUserResponse() throws Exception {
        return Parser.toJson(new UserResponseData(EMAIL, NAME));
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JpaUserRepository jpaUserRepository;

    @AfterEach
    private void afterEach() {
        jpaUserRepository.deleteAll();
    }

    @Nested
    @DisplayName("register 메서드는")
    public final class Describe_register {
        private String getErrorResponse(final String errorMessage) throws Exception {
            return Parser.toJson(new ErrorResponse(USERS_URL, HttpMethod.POST.toString(), errorMessage));
        }

        private ResultActions subject(final String requestBody) throws Exception {
            return mockMvc.perform(
                post(USERS_URL)
                    .accept(MediaType.APPLICATION_JSON_UTF8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody));
        }

        @Test
        @DisplayName("비밀번호를 제외한 사용자 정보를 리턴한다.")
        public void it_returns_user_data_without_a_password() throws Exception {
            subject(getUserRequest(EMAIL, NAME, PASSWORD))
                .andExpect(status().isCreated())
                .andExpect(content().string(getUserResponse()));
        }

        @Nested
        @DisplayName("유효하지 않은 데이터를 입력한 경우")
        @TestInstance(TestInstance.Lifecycle.PER_CLASS)
        public final class Context_invalidData {
            private Stream<Arguments> methodSource() throws Exception {
                return Stream.of(
                    Arguments.of(
                        getUserRequest(null, NAME, PASSWORD),
                        getErrorResponse("이메일이 입력되지 않았습니다.")
                    ),
                    Arguments.of(
                        getUserRequest("", NAME, PASSWORD),
                        getErrorResponse("유효하지 않은 이메일 형식입니다.")
                    ),
                    Arguments.of(
                        getUserRequest(INVALID_EMAIL, NAME, PASSWORD),
                        getErrorResponse("유효하지 않은 이메일 형식입니다.")
                    ),
                    Arguments.of(
                        getUserRequest(EMAIL, null, PASSWORD),
                        getErrorResponse("이름이 입력되지 않았습니다.")
                    ),
                    Arguments.of(
                        getUserRequest(EMAIL, "", PASSWORD),
                        getErrorResponse("이름이 입력되지 않았습니다.")
                    ),
                    Arguments.of(
                        getUserRequest(EMAIL, NAME, null),
                        getErrorResponse("비밀번호가 입력되지 않았습니다.")
                    ),
                    Arguments.of(
                        getUserRequest(EMAIL, NAME, ""),
                        getErrorResponse("최소 한개 이상의 대소문자와 숫자, 특수문자를 포함한 8자 이상의 비밀번호를 입력해야합니다.")
                    ),
                    Arguments.of(
                        getUserRequest(EMAIL, NAME, INVALID_PASSWORD_LOWER_CASE),
                        getErrorResponse("최소 한개 이상의 대소문자와 숫자, 특수문자를 포함한 8자 이상의 비밀번호를 입력해야합니다.")
                    ),
                    Arguments.of(
                        getUserRequest(EMAIL, NAME, INVALID_PASSWORD_UPPER_CASE),
                        getErrorResponse("최소 한개 이상의 대소문자와 숫자, 특수문자를 포함한 8자 이상의 비밀번호를 입력해야합니다.")
                    ),
                    Arguments.of(
                        getUserRequest(EMAIL, NAME, INVALID_PASSWORD_NUMBER),
                        getErrorResponse("최소 한개 이상의 대소문자와 숫자, 특수문자를 포함한 8자 이상의 비밀번호를 입력해야합니다.")
                    ),
                    Arguments.of(
                        getUserRequest(EMAIL, NAME, INVALID_PASSWORD_SPECIAL_CASE),
                        getErrorResponse("최소 한개 이상의 대소문자와 숫자, 특수문자를 포함한 8자 이상의 비밀번호를 입력해야합니다.")
                    )
                );
            }

            @MethodSource("methodSource")
            @DisplayName("입력 데이터가 잘못되었음을 알려준다.")
            @ParameterizedTest(name = "input=\"{0}\" output=\"{1}\"")
            public void it_notifies_that_input_data_is_invalid(final String input, final String output) throws Exception {
                subject(input)
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(output));
            }
        }

        @Nested
        @DisplayName("등록하려는 사용자의 이메일이 이미 존재하는 경우")
        public final class Context_emailAlreadyExists {
            @BeforeEach
            private void beforeEach() {
                userRepository.save(getUser());
            }

            @Test
            @DisplayName("이메일이 이미 존재함을 알려준다.")
            public void it_notifies_that_email_already_exists() throws Exception {
                subject(getUserRequest(EMAIL, NAME, PASSWORD))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(getErrorResponse(new UserEmailAlreadyExistException().getMessage())));
            }
        }
    }
}
