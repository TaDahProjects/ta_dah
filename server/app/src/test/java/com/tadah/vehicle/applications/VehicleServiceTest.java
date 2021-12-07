package com.tadah.vehicle.applications;

import com.tadah.vehicle.domains.entities.Vehicle;
import com.tadah.vehicle.domains.repositories.VehicleRepository;
import com.tadah.vehicle.domains.repositories.infra.JpaVehicleRepository;
import com.tadah.vehicle.exceptions.VehicleAlreadyExistException;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static com.tadah.vehicle.domains.entities.VehicleTest.VEHICLE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DataJpaTest
@DisplayName("VehicleService 클래스")
public class VehicleServiceTest {
    private final VehicleService vehicleService;

    @Autowired
    private JpaVehicleRepository jpaVehicleRepository;

    public VehicleServiceTest(@Autowired final VehicleRepository vehicleRepository) {
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
}