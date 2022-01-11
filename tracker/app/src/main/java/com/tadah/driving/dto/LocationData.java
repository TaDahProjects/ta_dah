package com.tadah.driving.dto;

import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.geolatte.geom.C2D;
import org.geolatte.geom.Geometry;
import org.geolatte.geom.codec.Wkt;
import org.geolatte.geom.crs.CrsRegistry;

@Generated
@Getter
@NoArgsConstructor
public class LocationData {
    private Long id;

    private Geometry<C2D> location;

    public LocationData(final Long id, final String location) {
        this.id = id;
        this.location =  Wkt.fromWkt(location, CrsRegistry.getProjectedCoordinateReferenceSystemForEPSG(5179));
    }
}
