package com.tadah.driving.domains.repositories;

import com.tadah.driving.domains.entities.Driving;

public interface DrivingRepository {
    /**
     * 운행정보를 생성한다.
     *
     * @param driving 생성할 운행 정보
     * @return 생성한 운행정보
     */
    Driving save(final Driving driving);
}
