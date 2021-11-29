package com.tadah.auth.controllers;

import com.tadah.auth.dtos.SessionRequestData;
import com.tadah.auth.dtos.SessionResponseData;
import com.tadah.auth.exceptions.LoginFailException;
import com.tadah.auth.utils.JwtUtil;
import com.tadah.common.dtos.ErrorResponse;
import com.tadah.user.domain.entities.User;
import com.tadah.user.domain.repositories.UserRepository;
import com.tadah.user.domain.repositories.infra.JpaUserRepository;
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

import static com.tadah.user.UserConstants.EMAIL;
import static com.tadah.user.UserConstants.INVALID_EMAIL;
import static com.tadah.user.UserConstants.INVALID_PASSWORD_LOWER_CASE;
import static com.tadah.user.UserConstants.INVALID_PASSWORD_NUMBER;
import static com.tadah.user.UserConstants.INVALID_PASSWORD_SPECIAL_CASE;
import static com.tadah.user.UserConstants.INVALID_PASSWORD_UPPER_CASE;
import static com.tadah.user.UserConstants.PASSWORD;
import static com.tadah.user.UserConstants.RIDER;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@ExtendWith(SpringExtension.class)
@DisplayName("SessionController 클래스")
public final class SessionControllerTest {
    private static final String SESSION_URL = "/session";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JpaUserRepository jpaUserRepository;

    @AfterEach
    private void afterEach() {
        jpaUserRepository.deleteAll();
    }

    @Nested
    @DisplayName("login 메서드는")
    public final class Describe_login {
        private ResultActions subject(final String requestBody) throws Exception {
            return mockMvc.perform(
                post(SESSION_URL)
                    .accept(MediaType.APPLICATION_JSON_UTF8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody));
        }

        @Nested
        @DisplayName("이메일에 해당하는 사용자가 없는 경우")
        public final class Context_invalidEmail {
            @Test
            @DisplayName("로그인에 실패하였음을 알려준다.")
            public void it_notifies_that_the_user_login_failed() throws Exception {
                subject(Parser.toJson(new SessionRequestData("invalid" + EMAIL, PASSWORD)))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(Parser.toJson(new ErrorResponse(SESSION_URL, HttpMethod.POST.toString(), new LoginFailException().getMessage()))));
            }
        }

        @Nested
        @DisplayName("비밀번호가 일치하지 않은 경우")
        public final class Context_invalidPassword {
            @Test
            @DisplayName("로그인에 실패하였음을 알려준다.")
            public void it_notifies_that_the_user_login_failed() throws Exception {
                subject(Parser.toJson(new SessionRequestData(EMAIL, PASSWORD + "invalid")))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(Parser.toJson(new ErrorResponse(SESSION_URL, HttpMethod.POST.toString(), new LoginFailException().getMessage()))));
            }
        }

        @Nested
        @DisplayName("로그인 정보가 일치하는 경우")
        public final class Context_validLoginInfo {
            private String token;

            @BeforeEach
            private void beforeEach() {
                final User savedUser = userRepository.save(RIDER);
                this.token = jwtUtil.encode(savedUser.getId());
            }

            @Test
            @DisplayName("토큰을 리턴한다.")
            public void it_returns_a_token() throws Exception {
                subject(Parser.toJson(new SessionRequestData(EMAIL, PASSWORD)))
                    .andExpect(status().isCreated())
                    .andExpect(content().string(Parser.toJson(new SessionResponseData(token))));
            }
        }

        @Nested
        @DisplayName("유효하지 않은 데이터를 입력한 경우")
        @TestInstance(TestInstance.Lifecycle.PER_CLASS)
        public final class Context_invalidData {
            private Stream<Arguments> methodSource() throws Exception {
                return Stream.of(
                    Arguments.of(Parser.toJson(new SessionRequestData(null, PASSWORD)), "이메일이 입력되지 않았습니다."),
                    Arguments.of(Parser.toJson(new SessionRequestData("", PASSWORD)), "유효하지 않은 이메일 형식입니다."),
                    Arguments.of(Parser.toJson(new SessionRequestData(INVALID_EMAIL, PASSWORD)), "유효하지 않은 이메일 형식입니다."),
                    Arguments.of(Parser.toJson(new SessionRequestData(EMAIL, null)), "비밀번호가 입력되지 않았습니다."),
                    Arguments.of(Parser.toJson(new SessionRequestData(EMAIL, "")), "최소 한개 이상의 대소문자와 숫자, 특수문자를 포함한 8자 이상의 비밀번호를 입력해야합니다."),
                    Arguments.of(Parser.toJson(new SessionRequestData(EMAIL, INVALID_PASSWORD_LOWER_CASE)), "최소 한개 이상의 대소문자와 숫자, 특수문자를 포함한 8자 이상의 비밀번호를 입력해야합니다."),
                    Arguments.of(Parser.toJson(new SessionRequestData(EMAIL, INVALID_PASSWORD_UPPER_CASE)), "최소 한개 이상의 대소문자와 숫자, 특수문자를 포함한 8자 이상의 비밀번호를 입력해야합니다."),
                    Arguments.of(Parser.toJson(new SessionRequestData(EMAIL, INVALID_PASSWORD_NUMBER)), "최소 한개 이상의 대소문자와 숫자, 특수문자를 포함한 8자 이상의 비밀번호를 입력해야합니다."),
                    Arguments.of(Parser.toJson(new SessionRequestData(EMAIL, INVALID_PASSWORD_SPECIAL_CASE)), "최소 한개 이상의 대소문자와 숫자, 특수문자를 포함한 8자 이상의 비밀번호를 입력해야합니다.")
                );
            }

            @MethodSource("methodSource")
            @DisplayName("입력 데이터가 잘못되었음을 알려준다.")
            @ParameterizedTest(name = "input=\"{0}\" errorMessage=\"{1}\"")
            public void it_notifies_that_input_data_is_invalid(final String input, final String errorMessage) throws Exception {
                subject(input)
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(Parser.toJson(new ErrorResponse(SESSION_URL, HttpMethod.POST.toString(), errorMessage))));
            }
        }
    }
}
