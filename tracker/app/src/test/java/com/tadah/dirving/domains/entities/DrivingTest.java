package com.tadah.dirving.domains.entities;

import com.tadah.driving.domains.entities.Driving;
import org.geolatte.geom.C2D;
import org.geolatte.geom.Geometries;
import org.geolatte.geom.Point;
import org.geolatte.geom.crs.CrsRegistry;
import org.junit.jupiter.api.DisplayName;

@DisplayName("Driving 클래스")
public final class DrivingTest {
    public static final Long USER_ID = 1L;
    private static final Double X = 531371.8436449164;
    private static final Double Y = 967246.4762861007;
    public static final Point<C2D> POINT = Geometries.mkPoint(new C2D(X, Y), CrsRegistry.getProjectedCoordinateReferenceSystemForEPSG(5179));
    public static final Driving DRIVING = new Driving(USER_ID, POINT);
}
