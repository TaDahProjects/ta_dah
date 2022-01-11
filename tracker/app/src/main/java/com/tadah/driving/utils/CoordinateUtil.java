package com.tadah.driving.utils;

import org.geolatte.geom.C2D;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Geometries;
import org.geolatte.geom.Point;
import org.geolatte.geom.crs.CoordinateReferenceSystems;
import org.geolatte.geom.crs.CrsRegistry;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.springframework.stereotype.Component;

/**
 * 좌표계 변환을 담당한다
 */
@Component
public final class CoordinateUtil {
    private final MathTransform toGpsTransForm;
    private final MathTransform fromGpsTransForm;
    private final GeometryFactory geometryFactory;

    public CoordinateUtil() throws FactoryException {
        final CoordinateReferenceSystem mpsCoordinateSystem = CRS.decode("EPSG:5179");
        final CoordinateReferenceSystem gpsCoordinateSystem = CRS.decode("EPSG:4326");

        geometryFactory = JTSFactoryFinder.getGeometryFactory();
        toGpsTransForm = CRS.findMathTransform(mpsCoordinateSystem, gpsCoordinateSystem);
        fromGpsTransForm = CRS.findMathTransform(gpsCoordinateSystem, mpsCoordinateSystem);
    }

    private Coordinate transForm(final Coordinate coordinate, final MathTransform transform) throws TransformException {
        final Geometry sourceGeometry = geometryFactory.createPoint(coordinate);
        final Geometry targetGeometry = JTS.transform(sourceGeometry, transform);
        return targetGeometry.getCoordinate();
    }

    /**
     * GPS 데이터의 좌표계를 변환한다
     *
     * @param latitude 위도
     * @param longitude 경도
     * @return EPSG 5179 좌표
     * @throws TransformException 좌표계 변환에 실패한 경우
     */
    public Point<C2D> fromGps(final Double latitude, final Double longitude) throws TransformException {
        final Coordinate coordinate = transForm(new Coordinate(latitude, longitude), fromGpsTransForm);
        return Geometries.mkPoint(new C2D(coordinate.y, coordinate.x), CrsRegistry.getProjectedCoordinateReferenceSystemForEPSG(5179));
    }

    /**
     * 위치 정보를 GPS 좌표계로 변환한다
     *
     * @param x x
     * @param y y
     * @return GPS 좌표
     * @throws TransformException 좌표계 변환에 실패한 경우
     */
    public Point<G2D> toGps(final Double x, final Double y) throws TransformException {
        final Coordinate coordinate = transForm(new Coordinate(y, x), toGpsTransForm);
        return Geometries.mkPoint(new G2D(coordinate.y, coordinate.x), CoordinateReferenceSystems.WGS84);
    }
}
