package com.tadah.driving.domains.repositories;

import com.tadah.driving.domains.entities.Driving;
import com.tadah.driving.domains.repositories.infra.JpaDrivingRepository;
import com.tadah.driving.dtos.PointData;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static com.tadah.driving.domains.entities.DrivingTest.AFTER_MAP_MATCH;
import static com.tadah.driving.domains.entities.DrivingTest.BEFORE_MAP_MATCH;
import static com.tadah.driving.domains.entities.DrivingTest.DRIVING;
import static com.tadah.driving.domains.entities.DrivingTest.POINT;
import static com.tadah.driving.domains.entities.DrivingTest.USER_ID;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("DrivingRepository 클래스")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DrivingRepositoryTest {
    @Autowired
    private DrivingRepository drivingRepository;

    @Autowired
    private JpaDrivingRepository jpaDrivingRepository;

    @Nested
    @DisplayName("save 메서드는")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    public final class Describe_save {
        private Driving subject() {
            return drivingRepository.save(DRIVING);
        }

        @AfterAll
        private void afterAll() {
            jpaDrivingRepository.deleteAll();
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

    @Nested
    @DisplayName("find 메서드는")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    public final class Describe_find {
        private Driving driving;

        private Optional<Driving> subject() {
            return drivingRepository.find(USER_ID);
        }

        @BeforeAll
        private void beforeAll() {
            driving = new Describe_save().subject();
        }

        @AfterAll
        private void afterAll() {
            jpaDrivingRepository.deleteAll();
        }

        @Test
        @DisplayName("운행정보를 찾는다.")
        public void it_finds_a_driving_data() {
            assertThat(subject())
                .isPresent();
        }

        @Nested
        @DisplayName("드라이버가 운행을 종료한 경우")
        @TestInstance(TestInstance.Lifecycle.PER_CLASS)
        public final class Context_stopDriving {
            @BeforeAll
            private void beforeAll() {
                driving.stopDriving();
                drivingRepository.save(driving);
            }

            @Test
            @DisplayName("운행을 종료하였음을 알려준다.")
            public void it_notifies_that_driving_is_closed() {
                assertThat(subject())
                    .isEmpty();
            }
        }
    }

    @Nested
    @DisplayName("update 메서드는")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    public final class Describe_update {
        private Driving driving;

        private void subject() {
            drivingRepository.update(driving.getId(), POINT);
        }

        @BeforeAll
        private void beforeAll() {
            driving = new Describe_save().subject();
        }

        @AfterAll
        private void afterAll() {
            jpaDrivingRepository.deleteAll();
        }

        @Test
        @DisplayName("위치정보를 업데이트한다")
        public void it_updates_the_location_data() {
            subject();

            assertThat(jpaDrivingRepository.findById(driving.getId()))
                .isPresent()
                .get()
                .matches(driving -> driving.getPath().getEndPosition().equals(POINT.getPosition()));
        }
    }

    @Nested
    @DisplayName("mapMatch 메서드는")
    public final class Describe_mapMatch {
        private PointData subject() {
            return drivingRepository.mapMatch(BEFORE_MAP_MATCH);
        }

        @Test
        @DisplayName("가장 가까운 도로 정보를 찾는다.")
        public void it_finds_a_closest_edge() {
            assertThat(subject())
                .matches(pointData -> pointData.getPoint().equals(AFTER_MAP_MATCH));
        }
    }
}
