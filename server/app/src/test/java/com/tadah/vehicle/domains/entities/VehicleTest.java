package com.tadah.vehicle.domains.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static com.tadah.user.domains.entities.UserTest.USER_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Vehicle 클래스")
public final class VehicleTest {
    public static final Double LATITUDE = 37.487964946;
    public static final Double LONGITUDE = 127.065536349;
    public static final Vehicle VEHICLE = new Vehicle(USER_ID);

    public static Double getLatitude() {
        return VEHICLE.getLatitude() == null ? LATITUDE : VEHICLE.getLatitude() + 1;
    }

    public static Double getLongitude() {
        return VEHICLE.getLongitude() == null ? LONGITUDE : VEHICLE.getLongitude() + 1;
    }

    public static Vehicle setDriving(final Vehicle vehicle, final boolean isDriving) {
        if (vehicle.isDriving() != isDriving) {
            vehicle.toggleDriving();
        }
        return vehicle;
    }

    public static Vehicle getVehicle(final boolean isDriving) {
        return setDriving(VEHICLE, isDriving);
    }


    @Nested
    @DisplayName("toggleDriving 메서드는")
    public final class Describe_toggleDriving {
        private void subject() {
            VEHICLE.toggleDriving();
        }

        @RepeatedTest(2)
        @DisplayName("차량의 운행상태를 변경한다.")
        public void it_changes_the_driving_status_of_the_vehicle() {
            final boolean isDriving = VEHICLE.isDriving();

            subject();

            assertThat(VEHICLE.isDriving())
                .isNotEqualTo(isDriving);
        }
    }

    @Nested
    @DisplayName("updateLocation 메서드는")
    public final class Describe_updateLocation {
        private void subject(final Double latitude, final Double longitude) {
            VEHICLE.updateLocation(latitude, longitude);
        }

        @Test
        @DisplayName("차량의 위치를 업데이트한다.")
        public void it_updates_the_location_of_the_vehicle() {
            final Double latitude = getLatitude();
            final Double longitude = getLongitude();

            subject(latitude, longitude);

            assertThat(VEHICLE.getLatitude())
                .isEqualTo(latitude);

            assertThat(VEHICLE.getLongitude())
                .isEqualTo(longitude);
        }
    }
}
