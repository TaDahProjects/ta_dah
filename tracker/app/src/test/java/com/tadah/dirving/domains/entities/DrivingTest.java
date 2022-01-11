package com.tadah.dirving.domains.entities;

import com.tadah.driving.domains.entities.Driving;
import org.geolatte.geom.C2D;
import org.geolatte.geom.Geometries;
import org.geolatte.geom.Point;
import org.geolatte.geom.crs.CoordinateReferenceSystem;
import org.geolatte.geom.crs.CrsRegistry;
import org.junit.jupiter.api.DisplayName;

@DisplayName("Driving 클래스")
public final class DrivingTest {
    private static final CoordinateReferenceSystem<C2D> COORDINATE_REFERENCE_SYSTEM = CrsRegistry.getProjectedCoordinateReferenceSystemForEPSG(5179);

    public static final Long USER_ID = 1L;
    public static final Double LATITUDE = 28.6D;
    public static final Double LONGITUDE = 122.71D;
    public static final Double X = 531371.8436449164;
    public static final Double Y = 967246.4762861007;

    private static final C2D POSITION = new C2D(X, Y);

    public static final Point<C2D> POINT = Geometries.mkPoint(POSITION, COORDINATE_REFERENCE_SYSTEM);
    public static final Driving DRIVING = new Driving(USER_ID, POINT);
    public static final Point<C2D> BEFORE_MAP_MATCH = Geometries.mkPoint(POSITION, COORDINATE_REFERENCE_SYSTEM);
    public static final Point<C2D> AFTER_MAP_MATCH = Geometries.mkPoint(new C2D(432633.8046912643, 664900.2020061786), COORDINATE_REFERENCE_SYSTEM);
}
