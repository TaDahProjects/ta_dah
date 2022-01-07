package com.tadah.dirving.utils;

import com.tadah.driving.utils.CoordinateUtil;
import org.geolatte.geom.C2D;
import org.geolatte.geom.Point;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.opengis.referencing.FactoryException;

import java.util.Optional;

import static com.tadah.dirving.domains.entities.DrivingTest.POINT;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("CoordinateUtil 클래스")
public final class CoordinateUtilTest {
    private static final Double LATITUDE = 28.6;
    private static final Double LONGITUDE = 122.71;

    private final CoordinateUtil coordinateUtil;

    public CoordinateUtilTest() throws FactoryException {
        coordinateUtil = new CoordinateUtil();
    }

    @Nested
    @DisplayName("transform 메서드는")
    public final class Describe_transform {
        private Optional<Point<C2D>> subject() {
            return coordinateUtil.transform(LATITUDE, LONGITUDE);
        }

        @Test
        @DisplayName("좌표계 변환을 수행한다.")
        public void it_transform_coordinate_system() {
            assertThat(subject())
                .isPresent()
                .get()
                .matches(point -> point.getPosition().getX() == POINT.getPosition().getX())
                .matches(point -> point.getPosition().getY() == POINT.getPosition().getY());
        }
    }
}
