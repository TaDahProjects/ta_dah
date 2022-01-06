package com.tadah.driving.utils;

import org.geolatte.geom.C2D;
import org.geolatte.geom.Geometries;
import org.geolatte.geom.Point;
import org.geolatte.geom.crs.CrsRegistry;
import org.geolatte.geom.crs.ProjectedCoordinateReferenceSystem;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 좌표계 변환을 담당한다
 */
@Component
public final class CoordinateUtil {
    private static final ProjectedCoordinateReferenceSystem EPSG_5179 = CrsRegistry.getProjectedCoordinateReferenceSystemForEPSG(5179);

    private final MathTransform mathTransform;
    private final GeometryFactory geometryFactory;

    public CoordinateUtil() throws FactoryException {
        final CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:5179");
        final CoordinateReferenceSystem sourceCRS = CRS.decode("EPSG:4326");
        mathTransform = CRS.findMathTransform(sourceCRS, targetCRS);
        geometryFactory = JTSFactoryFinder.getGeometryFactory();
    }

    /**
     * 좌표계 변환을 수행한다
     *
     * @param latitude 위도
     * @param longitude 경도
     * @return EPSG 5179 좌표
     */
    public Optional<Point<C2D>> transform(final Double latitude, final Double longitude) {
        try {
            final Coordinate sourceCoordinate = new Coordinate(latitude, longitude);
            final Geometry sourceGeometry = geometryFactory.createPoint(sourceCoordinate);

            final Geometry targetGeometry = JTS.transform(sourceGeometry, mathTransform);
            final Coordinate targetCoordinate = targetGeometry.getCoordinate();

            return Optional.of(Geometries.mkPoint(new C2D(targetCoordinate.y, targetCoordinate.x), EPSG_5179));
        } catch (Exception exception) {
            exception.printStackTrace();
            return Optional.empty();
        }
    }
}
