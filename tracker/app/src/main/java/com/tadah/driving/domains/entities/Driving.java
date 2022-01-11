package com.tadah.driving.domains.entities;

import com.tadah.driving.dto.LocationData;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.geolatte.geom.C2D;
import org.geolatte.geom.Geometries;
import org.geolatte.geom.LineString;
import org.geolatte.geom.Point;
import org.geolatte.geom.PositionSequenceBuilder;
import org.geolatte.geom.PositionSequenceBuilders;
import org.geolatte.geom.crs.CoordinateReferenceSystem;
import org.geolatte.geom.crs.CrsRegistry;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;

/**
 * 차량 운행정보를 정의한다.
 */
@Generated
@Entity
@Getter
@NoArgsConstructor
@NamedNativeQuery(
    name = "Driving.mapMatch",
    query = "select edge.id as id, st_astext(st_closestpoint(edge.geom, :point), 5179) as location" +
        " from (select gid as id, geom as geom from ad0022 order by st_distance(:point, geom) limit 1)" +
        " as edge",
    resultSetMapping = "Driving.mapMatch"
)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "Driving.mapMatch",
        classes = {
            @ConstructorResult(
                targetClass = LocationData.class,
                columns = {
                    @ColumnResult(name = "id", type = Long.class),
                    @ColumnResult(name = "location", type = String.class)
                }
            )
        }
    )
})
public final class Driving {
  @Id
  @GeneratedValue
  private Long id;

  private Long userId;

  @Setter
  private boolean isDriving = true;

  @Column(columnDefinition = "geometry(LineString,5179)")
  private LineString<C2D> path;

  private static LineString<C2D> getLineString(final C2D point) {
      final PositionSequenceBuilder<C2D> positionSequenceBuilder = PositionSequenceBuilders.fixedSized(2, C2D.class);
      positionSequenceBuilder.add(point);
      positionSequenceBuilder.add(point);
      return Geometries.mkLineString(
          positionSequenceBuilder.toPositionSequence(),
          CrsRegistry.getProjectedCoordinateReferenceSystemForEPSG(5179));
  }

  public Driving(final Long userId, final Point<C2D> point) {
    this.userId = userId;
    this.path = getLineString(point.getPosition());
  }
}
