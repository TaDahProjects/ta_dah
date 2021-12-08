package com.tadah.vehicle.controllers;

import com.tadah.auth.domains.entities.Role;
import com.tadah.auth.domains.repositories.RoleRepository;
import com.tadah.auth.domains.repositories.infra.JpaRoleRepository;
import com.tadah.auth.utils.JwtUtil;
import com.tadah.common.dtos.ErrorResponse;
import com.tadah.user.domains.repositories.UserRepository;
import com.tadah.user.domains.repositories.infra.JpaUserRepository;
import com.tadah.utils.LoginFailTest;
import com.tadah.utils.Parser;
import com.tadah.vehicle.domains.entities.Vehicle;
import com.tadah.vehicle.domains.repositories.VehicleRepository;
import com.tadah.vehicle.domains.repositories.infra.JpaVehicleRepository;
import com.tadah.vehicle.dtos.DrivingRequestData;
import com.tadah.vehicle.exceptions.VehicleAlreadyExistException;
import com.tadah.vehicle.exceptions.VehicleNotDrivingException;
import com.tadah.vehicle.exceptions.VehicleNotFoundException;
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

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static com.tadah.user.domains.entities.UserTest.getUser;
import static com.tadah.vehicle.domains.entities.VehicleTest.LATITUDE;
import static com.tadah.vehicle.domains.entities.VehicleTest.LONGITUDE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@ExtendWith(SpringExtension.class)
@DisplayName("VehicleController 클래스")
public final class VehicleControllerTest {
    private static final String VEHICLES_URL = "/vehicles";
    private static final String DRIVING_URL = "/driving";
    private static final String DRIVER_ROLE = "DRIVER";

    private static String getDrivingRequest(final Double latitude, final Double longitude) throws Exception {
        return Parser.toJson(new DrivingRequestData(latitude, longitude));
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private JpaRoleRepository jpaRoleRepository;

    @Autowired
    private JpaUserRepository jpaUserRepository;

    @Autowired
    private JpaVehicleRepository jpaVehicleRepository;

    @AfterEach
    private void afterEach() {
        jpaRoleRepository.deleteAll();
        jpaUserRepository.deleteAll();
        jpaVehicleRepository.deleteAll();
    }

    @Nested
    @DisplayName("create 메서드는")
    public final class Describe_create extends LoginFailTest {
        private Long userId;
        private String token;

        private String getErrorResponse() throws Exception {
            return Parser.toJson(new ErrorResponse(VEHICLES_URL, HttpMethod.POST.toString(), new VehicleAlreadyExistException().getMessage()));
        }

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
            this.userId = userRepository.save(getUser()).getId();
            this.token = jwtUtil.encode(userId);
        }

        @Test
        @DisplayName("차량을 생성한다.")
        public void it_creates_a_vehicles() throws Exception {
            subject(token)
                .andExpect(status().isCreated());

            final List<Role> roles = roleRepository.findAllByUserId(userId);

            assertThat(roles.size())
                .isEqualTo(1);

            assertThat(roles.get(0))
                .matches(Objects::nonNull)
                .matches(role -> role.getName().equals(DRIVER_ROLE))
                .matches(role -> role.getUserId().equals(userId));
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
                    .andExpect(content().string(getErrorResponse()));
            }
        }
    }

    @Nested
    @DisplayName("startDriving 메서드는")
    public final class Describe_startDriving extends LoginFailTest {
        private Long userId;
        private String token;

        public String getErrorResponse(final String errorMessage) throws Exception {
            return Parser.toJson(new ErrorResponse(VEHICLES_URL + DRIVING_URL, HttpMethod.POST.toString(), errorMessage));
        }

        public Describe_startDriving() throws Exception {
            super(
                mockMvc,
                post(VEHICLES_URL + DRIVING_URL)
                    .accept(MediaType.APPLICATION_JSON_UTF8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(getDrivingRequest(LATITUDE, LONGITUDE))
            );
        }

        private ResultActions subject(final String token, final String requestBody) throws Exception {
            return mockMvc.perform(
                post(VEHICLES_URL + DRIVING_URL)
                    .header(AUTHORIZATION_HEADER, TOKEN_PREFIX + token)
                    .accept(MediaType.APPLICATION_JSON_UTF8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
            );
        }

        @BeforeEach
        private void beforeEach() {
            this.userId = userRepository.save(getUser()).getId();
            roleRepository.save(new Role(userId, DRIVER_ROLE));
            this.token = jwtUtil.encode(userId);
        }

        @Nested
        @DisplayName("권한이 없는 경우")
        public final class Context_emptyRole {
            @BeforeEach
            private void beforeEach() {
                jpaRoleRepository.deleteAll();
            }

            @Test
            @DisplayName("권한이 필요함을 알려준다.")
            public void it_informs_that_role_is_required() throws Exception {
                subject(token, getDrivingRequest(LATITUDE, LONGITUDE))
                    .andExpect(status().isForbidden());
            }
        }

        @Nested
        @DisplayName("권한이 올바르지 않은 경우")
        public final class Context_invalidRole {
            @BeforeEach
            private void beforeEach() {
                jpaRoleRepository.deleteAll();
                roleRepository.save(new Role(userId, "INVALID"));
            }

            @Test
            @DisplayName("잘못된 권한임을 알려준다.")
            public void it_informs_that_role_is_invalid() throws Exception {
                subject(token, getDrivingRequest(LATITUDE, LONGITUDE))
                    .andExpect(status().isForbidden());
            }
        }

        @Nested
        @DisplayName("유효하지 않은 데이터를 입력한 경우")
        @TestInstance(TestInstance.Lifecycle.PER_CLASS)
        public final class Context_invalidData {
            private Stream<Arguments> methodSource() throws Exception {
                return Stream.of(
                    Arguments.of(
                        getDrivingRequest(null, LONGITUDE),
                        getErrorResponse("위도가 입력되지 않았습니다.")
                    ),
                    Arguments.of(
                        getDrivingRequest(-100D, LONGITUDE),
                        getErrorResponse("위도 범위를 벗어났습니다.")
                    ),
                    Arguments.of(
                        getDrivingRequest(100D, LONGITUDE),
                        getErrorResponse("위도 범위를 벗어났습니다.")
                    ),
                    Arguments.of(
                        getDrivingRequest(LATITUDE, null),
                        getErrorResponse("경도가 입력되지 않았습니다.")
                    ),
                    Arguments.of(
                        getDrivingRequest(LATITUDE, -200D),
                        getErrorResponse("경도 범위를 벗어났습니다.")
                    ),
                    Arguments.of(
                        getDrivingRequest(LATITUDE, 200D),
                        getErrorResponse("경도 범위를 벗어났습니다.")
                    )
                );
            }

            @MethodSource("methodSource")
            @DisplayName("입력 데이터가 잘못되었음을 알려준다.")
            @ParameterizedTest(name = "input=\"{0}\" output=\"{1}\"")
            public void it_notifies_that_input_data_is_invalid(final String input, final String output) throws Exception {
                subject(token, input)
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(output));
            }
        }

        @Nested
        @DisplayName("차량이 존재하지 않는 경우")
        public final class Context_vehicleNotExist {
            @Test
            @DisplayName("차량이 존재하지 않음을 알려준다.")
            public void it_notifies_that_vehicle_not_exist() throws Exception {
                subject(token, getDrivingRequest(LATITUDE, LONGITUDE))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string(getErrorResponse(new VehicleNotFoundException().getMessage())));
            }
        }

        @Nested
        @DisplayName("차량이 존재하는 경우")
        public final class Context_vehicleExist {
            @BeforeEach
            private void beforeEach() {
                vehicleRepository.save(new Vehicle(userId));
            }

            @Test
            @DisplayName("차량 운행을 시작한다.")
            public void it_starts_the_driving() throws Exception {
                subject(token, getDrivingRequest(LATITUDE, LONGITUDE))
                    .andExpect(status().isCreated());
            }
        }
    }


    @Nested
    @DisplayName("stopDriving 메서드는")
    public final class Describe_stopDriving extends LoginFailTest {
        private Long userId;
        private String token;

        public String getErrorResponse(final String errorMessage) throws Exception {
            return Parser.toJson(new ErrorResponse(VEHICLES_URL + DRIVING_URL, HttpMethod.DELETE.toString(), errorMessage));
        }

        public Describe_stopDriving() throws Exception {
            super(
                mockMvc,
                delete(VEHICLES_URL + DRIVING_URL)
                    .accept(MediaType.APPLICATION_JSON_UTF8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(getDrivingRequest(LATITUDE, LONGITUDE))
            );
        }

        private ResultActions subject(final String token, final String requestBody) throws Exception {
            return mockMvc.perform(
                delete(VEHICLES_URL + DRIVING_URL)
                    .header(AUTHORIZATION_HEADER, TOKEN_PREFIX + token)
                    .accept(MediaType.APPLICATION_JSON_UTF8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
            );
        }

        @BeforeEach
        private void beforeEach() {
            this.userId = userRepository.save(getUser()).getId();
            roleRepository.save(new Role(userId, DRIVER_ROLE));
            this.token = jwtUtil.encode(userId);
        }

        @Nested
        @DisplayName("권한이 없는 경우")
        public final class Context_emptyRole {
            @BeforeEach
            private void beforeEach() {
                jpaRoleRepository.deleteAll();
            }

            @Test
            @DisplayName("권한이 필요함을 알려준다.")
            public void it_informs_that_role_is_required() throws Exception {
                subject(token, getDrivingRequest(LATITUDE, LONGITUDE))
                    .andExpect(status().isForbidden());
            }
        }

        @Nested
        @DisplayName("권한이 올바르지 않은 경우")
        public final class Context_invalidRole {
            @BeforeEach
            private void beforeEach() {
                jpaRoleRepository.deleteAll();
                roleRepository.save(new Role(userId, "INVALID"));
            }

            @Test
            @DisplayName("잘못된 권한임을 알려준다.")
            public void it_informs_that_role_is_invalid() throws Exception {
                subject(token, getDrivingRequest(LATITUDE, LONGITUDE))
                    .andExpect(status().isForbidden());
            }
        }

        @Nested
        @DisplayName("유효하지 않은 데이터를 입력한 경우")
        @TestInstance(TestInstance.Lifecycle.PER_CLASS)
        public final class Context_invalidData {
            private Stream<Arguments> methodSource() throws Exception {
                return Stream.of(
                    Arguments.of(
                        getDrivingRequest(null, LONGITUDE),
                        getErrorResponse("위도가 입력되지 않았습니다.")
                    ),
                    Arguments.of(
                        getDrivingRequest(-100D, LONGITUDE),
                        getErrorResponse("위도 범위를 벗어났습니다.")
                    ),
                    Arguments.of(
                        getDrivingRequest(100D, LONGITUDE),
                        getErrorResponse("위도 범위를 벗어났습니다.")
                    ),
                    Arguments.of(
                        getDrivingRequest(LATITUDE, null),
                        getErrorResponse("경도가 입력되지 않았습니다.")
                    ),
                    Arguments.of(
                        getDrivingRequest(LATITUDE, -200D),
                        getErrorResponse("경도 범위를 벗어났습니다.")
                    ),
                    Arguments.of(
                        getDrivingRequest(LATITUDE, 200D),
                        getErrorResponse("경도 범위를 벗어났습니다.")
                    )
                );
            }

            @MethodSource("methodSource")
            @DisplayName("입력 데이터가 잘못되었음을 알려준다.")
            @ParameterizedTest(name = "input=\"{0}\" output=\"{1}\"")
            public void it_notifies_that_input_data_is_invalid(final String input, final String output) throws Exception {
                subject(token, input)
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(output));
            }
        }

        @Nested
        @DisplayName("차량이 존재하지 않는 경우")
        public final class Context_vehicleNotExist {
            @Test
            @DisplayName("차량이 존재하지 않음을 알려준다.")
            public void it_notifies_that_vehicle_not_exist() throws Exception {
                subject(token, getDrivingRequest(LATITUDE, LONGITUDE))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string(getErrorResponse(new VehicleNotFoundException().getMessage())));
            }
        }

        @Nested
        @DisplayName("차량이 존재하는 경우")
        public final class Context_vehicleExist {
            @BeforeEach
            private void beforeEach() {
                vehicleRepository.save(new Vehicle(userId));
            }

            @Test
            @DisplayName("차량 운행을 종료한다.")
            public void it_stops_the_driving() throws Exception {
                subject(token, getDrivingRequest(LATITUDE, LONGITUDE))
                    .andExpect(status().isNoContent());
            }
        }
    }

    @Nested
    @DisplayName("updateDriving 메서드는")
    public final class Describe_updateDriving extends LoginFailTest {
        private Long userId;
        private String token;
        private Vehicle vehicle;

        public String getErrorResponse(final String errorMessage) throws Exception {
            return Parser.toJson(new ErrorResponse(VEHICLES_URL + DRIVING_URL, HttpMethod.PUT.toString(), errorMessage));
        }

        public Describe_updateDriving() throws Exception {
            super(
                mockMvc,
                put(VEHICLES_URL + DRIVING_URL)
                    .accept(MediaType.APPLICATION_JSON_UTF8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(getDrivingRequest(LATITUDE, LONGITUDE))
            );
        }

        private ResultActions subject(final String token, final String requestBody) throws Exception {
            return mockMvc.perform(
                put(VEHICLES_URL + DRIVING_URL)
                    .header(AUTHORIZATION_HEADER, TOKEN_PREFIX + token)
                    .accept(MediaType.APPLICATION_JSON_UTF8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
            );
        }

        @BeforeEach
        private void beforeEach() {
            this.userId = userRepository.save(getUser()).getId();
            roleRepository.save(new Role(userId, DRIVER_ROLE));
            this.vehicle = vehicleRepository.save(new Vehicle(userId));
            this.token = jwtUtil.encode(userId);
        }

        @Nested
        @DisplayName("권한이 없는 경우")
        public final class Context_emptyRole {
            @BeforeEach
            private void beforeEach() {
                jpaRoleRepository.deleteAll();
            }

            @Test
            @DisplayName("권한이 필요함을 알려준다.")
            public void it_informs_that_role_is_required() throws Exception {
                subject(token, getDrivingRequest(LATITUDE, LONGITUDE))
                    .andExpect(status().isForbidden());
            }
        }

        @Nested
        @DisplayName("권한이 올바르지 않은 경우")
        public final class Context_invalidRole {
            @BeforeEach
            private void beforeEach() {
                jpaRoleRepository.deleteAll();
                roleRepository.save(new Role(userId, "INVALID"));
            }

            @Test
            @DisplayName("잘못된 권한임을 알려준다.")
            public void it_informs_that_role_is_invalid() throws Exception {
                subject(token, getDrivingRequest(LATITUDE, LONGITUDE))
                    .andExpect(status().isForbidden());
            }
        }

        @Nested
        @DisplayName("유효하지 않은 데이터를 입력한 경우")
        @TestInstance(TestInstance.Lifecycle.PER_CLASS)
        public final class Context_invalidData {
            private Stream<Arguments> methodSource() throws Exception {
                return Stream.of(
                    Arguments.of(
                        getDrivingRequest(null, LONGITUDE),
                        getErrorResponse("위도가 입력되지 않았습니다.")
                    ),
                    Arguments.of(
                        getDrivingRequest(-100D, LONGITUDE),
                        getErrorResponse("위도 범위를 벗어났습니다.")
                    ),
                    Arguments.of(
                        getDrivingRequest(100D, LONGITUDE),
                        getErrorResponse("위도 범위를 벗어났습니다.")
                    ),
                    Arguments.of(
                        getDrivingRequest(LATITUDE, null),
                        getErrorResponse("경도가 입력되지 않았습니다.")
                    ),
                    Arguments.of(
                        getDrivingRequest(LATITUDE, -200D),
                        getErrorResponse("경도 범위를 벗어났습니다.")
                    ),
                    Arguments.of(
                        getDrivingRequest(LATITUDE, 200D),
                        getErrorResponse("경도 범위를 벗어났습니다.")
                    )
                );
            }

            @MethodSource("methodSource")
            @DisplayName("입력 데이터가 잘못되었음을 알려준다.")
            @ParameterizedTest(name = "input=\"{0}\" output=\"{1}\"")
            public void it_notifies_that_input_data_is_invalid(final String input, final String output) throws Exception {
                subject(token, input)
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(output));
            }
        }

        @Nested
        @DisplayName("차량이 존재하지 않는 경우")
        public final class Context_vehicleNotExist {
            @BeforeEach
            private void beforeEach() {
                jpaVehicleRepository.deleteAll();
            }

            @Test
            @DisplayName("차량이 존재하지 않음을 알려준다.")
            public void it_notifies_that_vehicle_not_exist() throws Exception {
                subject(token, getDrivingRequest(LATITUDE, LONGITUDE))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string(getErrorResponse(new VehicleNotFoundException().getMessage())));
            }
        }

        @Nested
        @DisplayName("차량 운행이 종료된 경우")
        public final class Context_notDriving {
            @BeforeEach
            private void beforeEach() {
                if (vehicle.isDriving()) {
                    vehicle.toggleDriving();
                    vehicle = vehicleRepository.save(vehicle);
                }
            }

            @Test
            @DisplayName("차량 운행이 종료되었음을 알려준다.")
            public void it_notifies_that_vehicle_not_driving() throws Exception {
                subject(token, getDrivingRequest(LATITUDE, LONGITUDE))
                    .andExpect(status().isConflict())
                    .andExpect(content().string(getErrorResponse(new VehicleNotDrivingException().getMessage())));
            }
        }

        @Nested
        @DisplayName("차량이 존재하는 경우")
        public final class Context_vehicleExist {
            @BeforeEach
            private void beforeEach() {
                if (!vehicle.isDriving()) {
                    vehicle.toggleDriving();
                    vehicle = vehicleRepository.save(vehicle);
                }
            }

            @Test
            @DisplayName("차량 위치를 업데이트한다.")
            public void it_updates_the_location() throws Exception {
                subject(token, getDrivingRequest(LATITUDE, LONGITUDE))
                    .andExpect(status().isOk());
            }
        }
    }
}
