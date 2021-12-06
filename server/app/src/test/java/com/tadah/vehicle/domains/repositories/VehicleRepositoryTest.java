package com.tadah.vehicle.domains.repositories;

import com.tadah.vehicle.domains.entities.Vehicle;
import com.tadah.vehicle.domains.repositories.infra.JpaVehicleRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static com.tadah.vehicle.domains.entities.VehicleTest.VEHICLE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@DisplayName("VehicleRepository 클래스")
public class VehicleRepositoryTest {
    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private JpaVehicleRepository jpaVehicleRepository;

    @AfterEach
    private void afterEach() {
        jpaVehicleRepository.deleteAll();
    }

    @Nested
    @DisplayName("save 메서드는")
    public final class Describe_save {
        private Vehicle subject() {
            return vehicleRepository.save(VEHICLE);
        }

        @Test
        @DisplayName("차량 정보를 저장한다.")
        public void it_saves_a_vehicle_data() {
            final Vehicle savedVehicle = subject();

            assertThat(jpaVehicleRepository.findById(savedVehicle.getId()))
                .isPresent()
                .get()
                .matches(vehicle -> VEHICLE.getUserId().equals(vehicle.getUserId()));
        }
    }
}
