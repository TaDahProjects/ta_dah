package com.tadah.dirving.utils;

import com.tadah.driving.utils.CoordinateUtil;
import org.assertj.core.data.Offset;
import org.geolatte.geom.C2D;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;

import static com.tadah.dirving.domains.entities.DrivingTest.POINT;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("CoordinateUtil 클래스")
public final class CoordinateUtilTest {
    private static final Double LATITUDE = 28.6D;
    private static final Double LONGITUDE = 122.71D;

    private final CoordinateUtil coordinateUtil;

    public CoordinateUtilTest() throws FactoryException {
        coordinateUtil = new CoordinateUtil();
    }

    @Nested
    @DisplayName("fromGps 메서드는")
    public final class Describe_fromGps {
        private Point<C2D> subject() throws TransformException {
            return coordinateUtil.fromGps(LATITUDE, LONGITUDE);
        }

        @Test
        @DisplayName("GPS 데이터의 좌표계를 변환한다")
        public void it_converts_the_coordinate_system_of_gps_data() throws TransformException {
            assertThat(subject())
                .matches(point -> point.getPosition().getX() == POINT.getPosition().getX())
                .matches(point -> point.getPosition().getY() == POINT.getPosition().getY());
        }
    }

    @Nested
    @DisplayName("toGps 메서드는")
    public final class Describe_toGps {
        private Point<G2D> subject() throws TransformException {
            return coordinateUtil.toGps(POINT.getPosition().getX(), POINT.getPosition().getY());
        }

        @Test
        @DisplayName("위치정보를 GPS 좌표계로 변환한다")
        public void it_converts_location_information_into_gps_coordinates() throws TransformException {
            final Point<G2D> point = subject();

            assertThat(point.getPosition().getLat())
                .isEqualTo(LATITUDE, Offset.offset(0.01d));
            assertThat(point.getPosition().getLon())
                .isEqualTo(LONGITUDE, Offset.offset(0.01d));
        }
    }
}
