package com.tadah.user.controllers;

import com.tadah.user.applications.UserService;
import com.tadah.user.domain.entities.User;
import com.tadah.utils.Parser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.tadah.user.UserConstants.RIDER;
import static com.tadah.user.UserConstants.RIDER_USER_DATA;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atMostOnce;
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

    @Nested
    @DisplayName("createUser 메서드는")
    public final class Describe_createUser {
        private ResultActions subject(final String requestBody) throws Exception {
            return mockMvc.perform(
                post("/users")
                    .accept(MediaType.APPLICATION_JSON_UTF8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
            );
        }

        @BeforeEach
        public void beforeEach() {
            when(userService.saveUser(any(User.class)))
                .thenReturn(RIDER);
        }

        @AfterEach
        public void afterEach() {
            verify(userService, atMostOnce())
            .saveUser(any(User.class));
        }

        @Test
        @DisplayName("User를 생성한다.")
        public void it_creates_the_user() throws Exception {
            subject(Parser.toJson(RIDER_USER_DATA))
                .andExpect(status().isCreated())
                .andExpect(
                    content().string(
                        Parser.toJson(RIDER)
                    ));
        }
    }
}
