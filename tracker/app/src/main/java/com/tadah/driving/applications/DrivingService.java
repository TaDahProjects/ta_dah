package com.tadah.driving.applications;

import com.tadah.driving.domains.repositories.DrivingRepository;
import com.tadah.driving.utils.CoordinateUtil;
import org.geolatte.geom.C2D;
import org.geolatte.geom.Point;
import org.opengis.referencing.operation.TransformException;
import org.springframework.stereotype.Service;

@Service
public final class DrivingService {
    private final CoordinateUtil coordinateUtil;
    private final DrivingRepository drivingRepository;

    public DrivingService(final CoordinateUtil coordinateUtil, final DrivingRepository drivingRepository) {
        this.coordinateUtil = coordinateUtil;
        this.drivingRepository = drivingRepository;
    }

    /**
     * 좌료계 편환 후 맵매칭 수행 결과를 리턴한다
     *
     * @param latitude 위도
     * @param longitude 경도
     * @return 맵매칭 수행한 위치 정보(EPSG5179)
     * @throws TransformException 좌표계 변환에 실패한 경우
     */
    public Point<C2D> transForm(final Double latitude, final Double longitude) throws TransformException {
        final Point<C2D> point = coordinateUtil.fromGps(latitude, longitude);
        return drivingRepository.mapMatch(point).getPoint();
    }
}
