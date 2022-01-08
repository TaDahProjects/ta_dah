package com.tadah.driving.domains.repositories;

import com.tadah.driving.domains.entities.Driving;
import org.geolatte.geom.C2D;
import org.geolatte.geom.Point;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Optional;

public interface DrivingRepository {
    /**
     * 운행정보를 생성한다.
     *
     * @param driving 생성할 운행 정보
     * @return 생성한 운행정보
     */
    Driving save(final Driving driving);

    /**
     * 드라이버 아이디를 이용하여 운행정보를 조회한다.
     *
     * @param userId 드라이버 아이디
     * @return 운행 정보
     */
    @Query(value = "select * from driving where user_id = :userId and is_driving = true", nativeQuery = true)
    Optional<Driving> findDriving(final Long userId);
}
