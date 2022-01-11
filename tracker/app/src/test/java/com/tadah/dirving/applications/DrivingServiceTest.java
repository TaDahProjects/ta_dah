package com.tadah.dirving.applications;

import com.tadah.driving.applications.DrivingService;
import com.tadah.driving.domains.repositories.DrivingRepository;
import com.tadah.driving.domains.repositories.infra.JpaDrivingRepository;
import com.tadah.driving.utils.CoordinateUtil;
import org.geolatte.geom.C2D;
import org.geolatte.geom.Point;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static com.tadah.dirving.domains.entities.DrivingTest.AFTER_MAP_MATCH;
import static com.tadah.dirving.domains.entities.DrivingTest.LATITUDE;
import static com.tadah.dirving.domains.entities.DrivingTest.LONGITUDE;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("DrivingRepository 클래스")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DrivingServiceTest {
    private final DrivingService drivingService;
    private final DrivingRepository drivingRepository;
    private final JpaDrivingRepository jpaDrivingRepository;

    public DrivingServiceTest(
        @Autowired final DrivingRepository drivingRepository,
        @Autowired final JpaDrivingRepository jpaDrivingRepository) throws FactoryException {
        this.drivingRepository = drivingRepository;
        this.jpaDrivingRepository = jpaDrivingRepository;
        this.drivingService = new DrivingService(new CoordinateUtil(), drivingRepository);
    }

    @AfterEach
    private void afterEach() {
        jpaDrivingRepository.deleteAll();
    }

    @Nested
    @DisplayName("transForm 메서드는")
    public final class Describe_transFrom {
        private Point<C2D> subject() throws TransformException {
            return drivingService.transForm(LATITUDE, LONGITUDE);
        }

        @Test
        @DisplayName("좌표계 변환 후 맵매칭 수행 결과를 리턴한다")
        public void it_returns_the_result_of_map_matching_after_coordinate_system_transformation() throws TransformException {
            assertThat(subject())
                .matches(point -> point.getPosition().getX() == AFTER_MAP_MATCH.getPosition().getX())
                .matches(point -> point.getPosition().getY() == AFTER_MAP_MATCH.getPosition().getY());
        }
    }
}
