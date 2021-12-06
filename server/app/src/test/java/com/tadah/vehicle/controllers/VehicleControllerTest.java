package com.tadah.vehicle.controllers;

import com.tadah.auth.domains.entities.Role;
import com.tadah.auth.domains.repositories.RoleRepository;
import com.tadah.auth.domains.repositories.infra.JpaRoleRepository;
import com.tadah.auth.utils.JwtUtil;
import com.tadah.common.dtos.ErrorResponse;
import com.tadah.user.domains.UserType;
import com.tadah.user.domains.repositories.UserRepository;
import com.tadah.user.domains.repositories.infra.JpaUserRepository;
import com.tadah.utils.LoginFailTest;
import com.tadah.utils.Parser;
import com.tadah.vehicle.domains.entities.Vehicle;
import com.tadah.vehicle.domains.repositories.VehicleRepository;
import com.tadah.vehicle.domains.repositories.infra.JpaVehicleRepository;
import com.tadah.vehicle.exceptions.VehicleAlreadyExistException;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.tadah.user.domains.entities.UserTest.PASSWORD;
import static com.tadah.user.domains.entities.UserTest.PASSWORD_ENCODER;
import static com.tadah.user.domains.entities.UserTest.USER;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@ExtendWith(SpringExtension.class)
@DisplayName("VehicleController 클래스")
public final class VehicleControllerTest {
    private static final String VEHICLES_URL = "/vehicles";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private JpaUserRepository jpaUserRepository;

    @Autowired
    private JpaVehicleRepository jpaVehicleRepository;

    @AfterEach
    private void afterEach() {
        jpaUserRepository.deleteAll();
        jpaVehicleRepository.deleteAll();
    }

    @Nested
    @DisplayName("create 메서드는")
    public final class Describe_create extends LoginFailTest {
        private Long userId;
        private String token;

        public Describe_create() {
            super(mockMvc, post(VEHICLES_URL));
        }

        private ResultActions subject(final String token) throws Exception {
            return mockMvc.perform(
                post(VEHICLES_URL)
                    .accept(MediaType.APPLICATION_JSON_UTF8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(AUTHORIZATION_HEADER, TOKEN_PREFIX + token));
        }

        @BeforeEach
        private void beforeEach() {
            USER.setPassword(PASSWORD, PASSWORD_ENCODER);
            this.userId = userRepository.save(USER).getId();
            this.token = jwtUtil.encode(userId);
        }

        @Test
        @DisplayName("차량을 생성한다.")
        public void it_creates_a_vehicles() throws Exception {
            subject(token)
                .andExpect(status().isCreated());
        }

        @Nested
        @DisplayName("차량을 이미 등록한 경우")
        public final class Context_vehicleAlreadyExists {
            @BeforeEach
            private void beforeEach() {
                vehicleRepository.save(new Vehicle(userId));
            }

            @Test
            @DisplayName("차량이 이미 존재함을 알려준다.")
            public void it_notifies_that_vehicle_already_exists() throws Exception {
                subject(token)
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(Parser.toJson(new ErrorResponse(VEHICLES_URL, HttpMethod.POST.toString(), new VehicleAlreadyExistException().getMessage()))));
            }
        }
    }
}
