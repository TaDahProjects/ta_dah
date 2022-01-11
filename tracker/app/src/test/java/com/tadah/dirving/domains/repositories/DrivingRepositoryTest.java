package com.tadah.dirving.domains.repositories;

import com.tadah.driving.domains.entities.Driving;
import com.tadah.driving.domains.repositories.DrivingRepository;
import com.tadah.driving.domains.repositories.infra.JpaDrivingRepository;
import com.tadah.driving.dto.PointData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static com.tadah.dirving.domains.entities.DrivingTest.AFTER_MAP_MATCH;
import static com.tadah.dirving.domains.entities.DrivingTest.BEFORE_MAP_MATCH;
import static com.tadah.dirving.domains.entities.DrivingTest.DRIVING;
import static com.tadah.dirving.domains.entities.DrivingTest.POINT;
import static com.tadah.dirving.domains.entities.DrivingTest.USER_ID;
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

    @Nested
    @DisplayName("findDriving 메서드는")
    public final class Describe_findDriving {
        private Optional<Driving> subject() {
            return drivingRepository.findDriving(USER_ID);
        }

        @Nested
        @DisplayName("드라이버가 운행을 시작한 경우")
        @TestInstance(TestInstance.Lifecycle.PER_CLASS)
        public final class Context_startDriving {
            @BeforeEach
            private void beforeEach() {
                new Describe_save().subject();
            }

            @Test
            @DisplayName("운행정보를 찾는다.")
            public void it_finds_a_driving_data() {
                assertThat(subject())
                    .isPresent();
            }
        }

        @Nested
        @DisplayName("드라이버가 운행을 종료한 경우")
        public final class Context_stopDriving {
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
    public final class Describe_update {
        private Long id;

        private void subject(final Long id) {
            drivingRepository.update(id, POINT, false);
        }

        @BeforeEach
        private void beforeEach() {
            id = new Describe_save().subject().getId();
        }

        @Test
        @DisplayName("현재위치를 업데이트한다.")
        public void it_updates_a_current_location() {
            subject(id);

            assertThat(jpaDrivingRepository.findById(id))
                .isPresent()
                .get()
                .matches(driving -> driving.getPath().getEndPosition().equals(POINT.getPosition()))
                .matches(driving -> !driving.isDriving());
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
