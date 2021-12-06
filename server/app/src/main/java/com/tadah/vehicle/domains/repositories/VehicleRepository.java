package com.tadah.vehicle.domains.repositories;

import com.tadah.vehicle.domains.entities.Vehicle;

public interface VehicleRepository {
    /**
     * 차량 정보를 저장한다.
     *
     * @param vehicle 저장할 사용자 정보
     * @return 저장한 사용자 정보
     */
    Vehicle save(final Vehicle vehicle);

    /**
     * 차량의 소유여부를 리턴한다.
     *
     * @param userId 차량 소유여부를 확인할 사용자
     * @return 차량의 소유여부
     */
    boolean existsByUserId(final Long userId);
}
