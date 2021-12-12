package com.tadah.vehicle.domains.repositories;

import com.tadah.vehicle.domains.entities.Vehicle;
import com.tadah.vehicle.domains.repositories.infra.JpaVehicleRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static com.tadah.user.domains.entities.UserTest.USER_ID;
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

    @Nested
    @DisplayName("existsByUserId 메서드는")
    public final class Describe_existsByUserId {
        private boolean subject() {
            return vehicleRepository.existsByUserId(USER_ID);
        }

        @Nested
        @DisplayName("차량이 존재하는 경우")
        public final class Context_vehicleExist {
            @BeforeEach
            private void beforeEach() {
                new Describe_save().subject();
            }

            @Test
            @DisplayName("차량이 존재하는것을 알려준다.")
            public void it_notifies_that_the_vehicle_exists() {
                assertThat(subject())
                    .isTrue();
            }
        }

        @Nested
        @DisplayName("차량이 존재하니 않는경우")
        public final class Context_vehicleNotExist {
            @Test
            @DisplayName("차량이 존재하지 않는것을 알려준다.")
            public void it_notifies_that_vehicle_does_not_exist() {
                assertThat(subject())
                    .isFalse();
            }
        }
    }

    @Nested
    @DisplayName("findByUserId 메서드는")
    public final class Describe_findByUserId {
        private Optional<Vehicle> subject() {
            return vehicleRepository.findByUserId(USER_ID);
        }

        @Nested
        @DisplayName("사용자 id에 해당하는 차량이 존재하는경우")
        public final class Context_vehicleExist {
            @BeforeEach
            private void beforeEach() {
                new Describe_save().subject();
            }

            @Test
            @DisplayName("차량정보를 리턴한다.")
            public void it_returns_a_vehicle_data() {
                assertThat(subject())
                    .isPresent()
                    .get()
                    .matches(vehicle -> VEHICLE.getUserId().equals(vehicle.getUserId()));
            }
        }

        @Nested
        @DisplayName("사용자 id에 해당하는 차량이 존재하지 않는 경우")
        public final class Context_vehicleNotExist {
            @Test
            @DisplayName("차량정보가 존재하지 않음을 알려준다.")
            public void it_notifies_that_vehicle_data_not_exist() {
                assertThat(subject())
                    .isEmpty();
            }
        }
    }
}
