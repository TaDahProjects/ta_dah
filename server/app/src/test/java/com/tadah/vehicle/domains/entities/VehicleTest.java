package com.tadah.vehicle.domains.entities;

import org.junit.jupiter.api.DisplayName;

import static com.tadah.user.domains.entities.UserTest.USER_ID;

@DisplayName("Vehicle 클래스")
public final class VehicleTest {
    public static final Double LATITUDE = 37.487964946;
    public static final Double LONGITUDE = 127.065536349;
    public static final Vehicle VEHICLE = new Vehicle(USER_ID);
}
