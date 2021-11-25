package com.tadah.location.controllers;

import com.tadah.auth.applications.AuthService;
import com.tadah.utils.LoginFailTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.tadah.auth.utils.JwtUtilTest.VALID_TOKEN;
import static com.tadah.user.UserConstants.DRIVER;
import static com.tadah.user.UserConstants.RIDER;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("LocationController 클래스")
@WebMvcTest(LocationController.class)
public final class LocationControllerTest {
    private static final String URL = "/locations";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @BeforeEach
    private void beforeEach() {
        reset(authService);
    }

    @Nested
    @DisplayName("create 메서드는")
    public final class Describe_create extends LoginFailTest {
        public Describe_create() {
            super(RIDER, mockMvc, authService, post(URL));
        }

        @Nested
        @DisplayName("올바른 토큰이 입력된 경우")
        public final class Context_validToken {
            @BeforeEach
            private void beforeEach() {
                when(authService.verifyToken(VALID_TOKEN))
                    .thenReturn(DRIVER);
            }

            @Test
            public void test() throws Exception {
                mockMvc.perform(
                        post(URL)
                            .accept(MediaType.APPLICATION_JSON_UTF8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header(AUTHORIZATION_HEADER, TOKEN_PREFIX + VALID_TOKEN))
                    .andExpect(status().isCreated());
            }
        }
    }
}
