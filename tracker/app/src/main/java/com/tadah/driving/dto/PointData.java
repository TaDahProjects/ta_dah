package com.tadah.driving.dto;

import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.geolatte.geom.C2D;
import org.geolatte.geom.Geometries;
import org.geolatte.geom.Point;
import org.geolatte.geom.codec.Wkt;
import org.geolatte.geom.crs.CrsRegistry;

/**
 * 맵매칭 결과를 저장한다
 */
@Generated
@Getter
@NoArgsConstructor
public class PointData {
    private Point<C2D> point;

    public PointData(final String point) {
        this.point = Geometries.mkPoint(
            Wkt.fromWkt(point, CrsRegistry.getProjectedCoordinateReferenceSystemForEPSG(5179)).getPositionN(0),
            CrsRegistry.getProjectedCoordinateReferenceSystemForEPSG(5179));
    }
}
