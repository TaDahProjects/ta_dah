package com.tadah.vehicle.domains.repositories;

import com.tadah.vehicle.domains.entities.Vehicle;

import java.util.Optional;

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

    /**
     * 사용자 아이디를 이용하여 차량을 조회한다.
     *
     * @param userId 조회할 차량의 소유자
     * @return 조회한 차량의 정보
     */
    Optional<Vehicle> findByUserId(final Long userId);
}
