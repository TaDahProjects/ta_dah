package com.tadah.dirving.domains.entities;

import com.tadah.driving.domains.entities.Driving;
import org.geolatte.geom.C2D;
import org.geolatte.geom.Geometries;
import org.geolatte.geom.Point;
import org.geolatte.geom.crs.CrsRegistry;
import org.junit.jupiter.api.DisplayName;

@DisplayName("Driving 클래스")
public final class DrivingTest {
    private static final Long USER_ID = 1L;
    private static final Double LATITUDE = 28.6;
    private static final Double LONGITUDE = 122.71;
    private static final Point<C2D> POINT = Geometries.mkPoint(new C2D(LATITUDE, LONGITUDE), CrsRegistry.getProjectedCoordinateReferenceSystemForEPSG(5179));
    public static final Driving DRIVING = new Driving(USER_ID, POINT);
}
