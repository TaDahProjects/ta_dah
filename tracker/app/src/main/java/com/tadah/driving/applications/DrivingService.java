package com.tadah.driving.applications;

import com.tadah.driving.domains.entities.Driving;
import com.tadah.driving.domains.repositories.DrivingRepository;
import com.tadah.driving.utils.CoordinateUtil;
import org.geolatte.geom.C2D;
import org.geolatte.geom.Point;
import org.opengis.referencing.operation.TransformException;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    /**
     * 차량 운행정보를 가져온다
     *
     * @param userId 드라이버 아이디
     * @return 차량 운행 정보
     */
    public Optional<Driving> get(final Long userId) {
        return drivingRepository.find(userId);
    }

    /**
     * 차량 운행을 시작한다
     *
     * @param driving 시작할 운행 정보
     * @return 시작한 운행 정보
     */
    public Driving start(final Driving driving) {
        return drivingRepository.save(driving);
    }

    /**
     * 차량 운행을 종료한다
     *
     * @param driving 종료할 운행 정보
     * @param point 종료 지점
     */
    public void stop(final Driving driving, final Point<C2D> point) {
        drivingRepository.update(driving.getId(), point, false);
    }

    /**
     * 위치 정보를 업데이트한다
     *
     * @param driving 차량 운행 정보
     * @param point 업데이트할 위치 정보
     */
    public void update(final Driving driving, final Point<C2D> point) {
        drivingRepository.update(driving.getId(), point, true);
    }
}
