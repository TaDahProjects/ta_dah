package com.tadah.driving.utils;

import org.assertj.core.data.Offset;
import org.geolatte.geom.C2D;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;

import static com.tadah.driving.domains.entities.DrivingTest.LATITUDE;
import static com.tadah.driving.domains.entities.DrivingTest.LONGITUDE;
import static com.tadah.driving.domains.entities.DrivingTest.X;
import static com.tadah.driving.domains.entities.DrivingTest.Y;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("CoordinateUtil 클래스")
public final class CoordinateUtilTest {
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
                .matches(point -> point.getPosition().getX() == X)
                .matches(point -> point.getPosition().getY() == Y);
        }
    }

    @Nested
    @DisplayName("toGps 메서드는")
    public final class Describe_toGps {
        private Point<G2D> subject() throws TransformException {
            return coordinateUtil.toGps(X, Y);
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
