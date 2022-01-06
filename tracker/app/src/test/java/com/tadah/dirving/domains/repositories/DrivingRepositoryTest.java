package com.tadah.dirving.domains.repositories;

import com.tadah.driving.domains.entities.Driving;
import com.tadah.driving.domains.repositories.DrivingRepository;
import com.tadah.driving.domains.repositories.infra.JpaDrivingRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static com.tadah.dirving.domains.entities.DrivingTest.DRIVING;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("DrivingRepository 클래스")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DrivingRepositoryTest {
    @Autowired
    private DrivingRepository drivingRepository;

    @Autowired
    private JpaDrivingRepository jpaDrivingRepository;

    @AfterEach
    private void afterEach() {
        jpaDrivingRepository.deleteAll();
    }

    @Nested
    @DisplayName("save 메서드는")
    public final class Describe_save {
        private Driving subject() {
            return drivingRepository.save(DRIVING);
        }

        @Test
        @DisplayName("운행정보를 저장한다.")
        public void it_saves_a_driving_data() {
            final Driving savedDriving = subject();

            assertThat(jpaDrivingRepository.findById(savedDriving.getId()))
                .isPresent()
                .get()
                .matches(driving -> DRIVING.getUserId().equals(driving.getUserId()));
        }
    }
}
