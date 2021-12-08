package com.tadah.vehicle.applications;

import com.tadah.vehicle.domains.entities.Vehicle;
import com.tadah.vehicle.domains.repositories.VehicleRepository;
import com.tadah.vehicle.domains.repositories.infra.JpaVehicleRepository;
import com.tadah.vehicle.exceptions.VehicleAlreadyExistException;
import com.tadah.vehicle.exceptions.VehicleNotDrivingException;
import com.tadah.vehicle.exceptions.VehicleNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static com.tadah.user.domains.entities.UserTest.USER_ID;
import static com.tadah.vehicle.domains.entities.VehicleTest.LATITUDE;
import static com.tadah.vehicle.domains.entities.VehicleTest.LONGITUDE;
import static com.tadah.vehicle.domains.entities.VehicleTest.VEHICLE;
import static com.tadah.vehicle.domains.entities.VehicleTest.getLatitude;
import static com.tadah.vehicle.domains.entities.VehicleTest.getLongitude;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DataJpaTest
@DisplayName("VehicleService 클래스")
public class VehicleServiceTest {
    private final VehicleService vehicleService;
    private final VehicleRepository vehicleRepository;

    @Autowired
    private JpaVehicleRepository jpaVehicleRepository;

    public VehicleServiceTest(@Autowired final VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
        this.vehicleService = new VehicleService(vehicleRepository);
    }

    @AfterEach
    private void afterEach() {
        jpaVehicleRepository.deleteAll();
    }

    @Nested
    @DisplayName("create 메서드는")
    public final class Describe_create {
        private Vehicle subject() {
            return vehicleService.create(VEHICLE);
        }

        @Test
        @DisplayName("차량을 생성한다.")
        public void it_creates_a_vehicles() {
            assertThat(subject())
                .isInstanceOf(Vehicle.class)
                .matches(vehicle -> VEHICLE.getUserId().equals(vehicle.getUserId()));
        }

        @Nested
        @DisplayName("차량이 이미 존재하는 경우")
        public final class Context_vehicleAlreadyExists {
            @BeforeEach
            private void beforeEach() {
                subject();
            }

            @Test
            @DisplayName("VehicleAlreadyExistException을 던진다.")
            public void it_throws_vehicle_already_exist_exception() {
                assertThatThrownBy(Describe_create.this::subject)
                    .isInstanceOf(VehicleAlreadyExistException.class);
            }
        }
    }

    @Nested
    @DisplayName("startDriving 메서드는")
    public final class Describe_startDriving {
        private Vehicle subject(final Double latitude, final Double longitude) {
            return vehicleService.startDriving(USER_ID, latitude, longitude);
        }

        @Nested
        @DisplayName("차량이 존재하는 경우")
        public final class Context_vehicleExist {
            @DisplayName("차량의 운행을 시작한다.")
            @ValueSource(booleans = {true, false})
            @ParameterizedTest(name = "isDriving = {0}")
            public void it_starts_the_driving(final boolean isDriving) {
                if (VEHICLE.isDriving() == isDriving) {
                    VEHICLE.toggleDriving();
                }
                vehicleRepository.save(VEHICLE);

                final Double latitude = getLatitude();
                final Double longitude = getLongitude();
                assertThat(subject(latitude, longitude))
                    .matches(Vehicle::isDriving)
                    .matches(vehicle -> latitude.equals(vehicle.getLatitude()))
                    .matches(vehicle -> longitude.equals(vehicle.getLongitude()));
            }
        }

        @Nested
        @DisplayName("차량이 존재하지 않는 경우")
        public final class Context_vehicleNotExist {
            @Test
            @DisplayName("VehicleNotFoundException을 던진다.")
            public void it_throws_vehicle_not_found_exception() {
                assertThatThrownBy(() -> subject(LATITUDE, LONGITUDE))
                    .isInstanceOf(VehicleNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("stopDriving 메서드는")
    public final class Describe_stopDriving {
        private Vehicle subject(final Double latitude, final Double longitude) {
            return vehicleService.stopDriving(USER_ID, latitude, longitude);
        }

        @Nested
        @DisplayName("차량이 존재하는 경우")
        public final class Context_vehicleExist {
            @DisplayName("차량의 운행을 종료한다.")
            @ValueSource(booleans = {true, false})
            @ParameterizedTest(name = "isDriving = {0}")
            public void it_starts_the_driving(final boolean isDriving) {
                if (VEHICLE.isDriving() == isDriving) {
                    VEHICLE.toggleDriving();
                }
                vehicleRepository.save(VEHICLE);

                final Double latitude = getLatitude();
                final Double longitude = getLongitude();
                assertThat(subject(latitude, longitude))
                    .matches(vehicle -> !vehicle.isDriving())
                    .matches(vehicle -> latitude.equals(vehicle.getLatitude()))
                    .matches(vehicle -> longitude.equals(vehicle.getLongitude()));
            }
        }

        @Nested
        @DisplayName("차량이 존재하지 않는 경우")
        public final class Context_vehicleNotExist {
            @Test
            @DisplayName("VehicleNotFoundException을 던진다.")
            public void it_throws_vehicle_not_found_exception() {
                assertThatThrownBy(() -> subject(LATITUDE, LONGITUDE))
                    .isInstanceOf(VehicleNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("updateLocation 메서드는")
    public final class Describe_updateLocation {
        private Vehicle subject(final Double latitude, final Double longitude) {
            return vehicleService.updateLocation(USER_ID, latitude, longitude);
        }

        @Nested
        @DisplayName("차량이 존재하는 경우")
        public final class Context_vehicleExist {
            @BeforeEach
            private void beforeEach() {
                if (!VEHICLE.isDriving()) {
                    VEHICLE.toggleDriving();
                }
                vehicleRepository.save(VEHICLE);
            }

            @Test
            @DisplayName("차량의 위치를 업데이트한다.")
            public void it_updates_the_location_of_the_vehicle() {
                final Double latitude = getLatitude();
                final Double longitude = getLongitude();
                assertThat(subject(latitude, longitude))
                    .matches(vehicle -> latitude.equals(vehicle.getLatitude()))
                    .matches(vehicle -> longitude.equals(vehicle.getLongitude()));
            }
        }

        @Nested
        @DisplayName("차량 운행이 종료된 경우")
        public final class Context_notDriving {
            @BeforeEach
            private void beforeEach() {
                if (VEHICLE.isDriving()) {
                    VEHICLE.toggleDriving();
                }
                vehicleRepository.save(VEHICLE);
            }

            @Test
            @DisplayName("VehicleNotDrivingException을 던진다.")
            public void it_throws_vehicle_not_driving_exception() {
                assertThatThrownBy(() -> subject(LATITUDE, LONGITUDE))
                    .isInstanceOf(VehicleNotDrivingException.class);
            }
        }

        @Nested
        @DisplayName("차량이 존재하지 않는 경우")
        public final class Context_vehicleNotExist {
            @Test
            @DisplayName("VehicleNotFoundException을 던진다.")
            public void it_throws_vehicle_not_found_exception() {
                assertThatThrownBy(() -> subject(LATITUDE, LONGITUDE))
                    .isInstanceOf(VehicleNotFoundException.class);
            }
        }
    }
}
