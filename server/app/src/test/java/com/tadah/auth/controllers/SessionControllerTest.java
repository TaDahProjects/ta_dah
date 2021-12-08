package com.tadah.auth.controllers;

import com.tadah.auth.dtos.SessionRequestData;
import com.tadah.auth.dtos.SessionResponseData;
import com.tadah.auth.exceptions.LoginFailException;
import com.tadah.auth.utils.JwtUtil;
import com.tadah.common.dtos.ErrorResponse;
import com.tadah.user.domains.repositories.UserRepository;
import com.tadah.user.domains.repositories.infra.JpaUserRepository;
import com.tadah.utils.Parser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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

import static com.tadah.user.controllers.UserControllerTest.INVALID_EMAIL;
import static com.tadah.user.controllers.UserControllerTest.INVALID_PASSWORD_LOWER_CASE;
import static com.tadah.user.controllers.UserControllerTest.INVALID_PASSWORD_NUMBER;
import static com.tadah.user.controllers.UserControllerTest.INVALID_PASSWORD_SPECIAL_CASE;
import static com.tadah.user.controllers.UserControllerTest.INVALID_PASSWORD_UPPER_CASE;
import static com.tadah.user.domains.entities.UserTest.EMAIL;
import static com.tadah.user.domains.entities.UserTest.PASSWORD;
import static com.tadah.user.domains.entities.UserTest.getUser;
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

    private static String getSessionRequest(final String email, final String password) throws Exception {
        return Parser.toJson(new SessionRequestData(email, password));
    }

    private static String getSessionResponse(final String token) throws Exception {
        return Parser.toJson(new SessionResponseData(token));
    }

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
        private String getErrorMessage(final String errorMessage) throws Exception {
            return Parser.toJson(new ErrorResponse(SESSION_URL, HttpMethod.POST.toString(), errorMessage));
        }

        private ResultActions subject(final String requestBody) throws Exception {
            return mockMvc.perform(
                post(SESSION_URL)
                    .accept(MediaType.APPLICATION_JSON_UTF8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody));
        }

        @Nested
        @DisplayName("입력데이터가 잘못된 경우")
        @TestInstance(TestInstance.Lifecycle.PER_CLASS)
        public final class Context_invalidInputData {
            private Stream<Arguments> methodSource() throws Exception {
                return Stream.of(
                    Arguments.of(
                        getSessionRequest("invalid" + EMAIL, PASSWORD),
                        getErrorMessage(new LoginFailException().getMessage())
                    ),
                    Arguments.of(
                        getSessionRequest(EMAIL, PASSWORD + "invalid"),
                        getErrorMessage(new LoginFailException().getMessage())
                    )
                );
            }

            @MethodSource("methodSource")
            @DisplayName("로그인에 실패하였음을 알려준다.")
            @ParameterizedTest(name = "input=\"{0}\" output=\"{1}\"")
            public void it_notifies_that_the_user_login_failed(final String input, final String output) throws Exception {
                subject(input)
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(output));
            }
        }

        @Nested
        @DisplayName("로그인 정보가 일치하는 경우")
        @TestInstance(TestInstance.Lifecycle.PER_CLASS)
        public final class Context_validLoginInfo {
            private Stream<Arguments> methodSource() throws Exception {
                final Long userId = userRepository.save(getUser()).getId();
                final String token = jwtUtil.encode(userId);
                return Stream.of(Arguments.of(getSessionRequest(EMAIL, PASSWORD), getSessionResponse(token)));
            }

            @DisplayName("토큰을 리턴한다.")
            @MethodSource("methodSource")
            @ParameterizedTest(name = "input=\"{0}\" output=\"{1}\"")
            public void it_returns_a_token(final String input, final String output) throws Exception {
                subject(input)
                    .andExpect(status().isCreated())
                    .andExpect(content().string(output));
            }
        }

        @Nested
        @DisplayName("유효하지 않은 데이터를 입력한 경우")
        @TestInstance(TestInstance.Lifecycle.PER_CLASS)
        public final class Context_invalidData {
            private Stream<Arguments> methodSource() throws Exception {
                return Stream.of(
                    Arguments.of(
                        getSessionRequest(null, PASSWORD),
                        getErrorMessage("이메일이 입력되지 않았습니다.")
                    ),
                    Arguments.of(
                        getSessionRequest("", PASSWORD),
                        getErrorMessage("유효하지 않은 이메일 형식입니다.")
                    ),
                    Arguments.of(
                        getSessionRequest(INVALID_EMAIL, PASSWORD),
                        getErrorMessage("유효하지 않은 이메일 형식입니다.")
                    ),
                    Arguments.of(
                        getSessionRequest(EMAIL, null),
                        getErrorMessage("비밀번호가 입력되지 않았습니다.")
                    ),
                    Arguments.of(
                        getSessionRequest(EMAIL, ""),
                        getErrorMessage("최소 한개 이상의 대소문자와 숫자, 특수문자를 포함한 8자 이상의 비밀번호를 입력해야합니다.")
                    ),
                    Arguments.of(
                        getSessionRequest(EMAIL, INVALID_PASSWORD_LOWER_CASE),
                        getErrorMessage("최소 한개 이상의 대소문자와 숫자, 특수문자를 포함한 8자 이상의 비밀번호를 입력해야합니다.")
                    ),
                    Arguments.of(
                        getSessionRequest(EMAIL, INVALID_PASSWORD_UPPER_CASE),
                        getErrorMessage("최소 한개 이상의 대소문자와 숫자, 특수문자를 포함한 8자 이상의 비밀번호를 입력해야합니다.")
                    ),
                    Arguments.of(
                        getSessionRequest(EMAIL, INVALID_PASSWORD_NUMBER),
                        getErrorMessage("최소 한개 이상의 대소문자와 숫자, 특수문자를 포함한 8자 이상의 비밀번호를 입력해야합니다.")
                    ),
                    Arguments.of(
                        getSessionRequest(EMAIL, INVALID_PASSWORD_SPECIAL_CASE),
                        getErrorMessage("최소 한개 이상의 대소문자와 숫자, 특수문자를 포함한 8자 이상의 비밀번호를 입력해야합니다.")
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
    }
}
