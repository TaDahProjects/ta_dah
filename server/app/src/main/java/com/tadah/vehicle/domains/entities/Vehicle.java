package com.tadah.vehicle.domains.entities;

import com.tadah.common.annotations.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 차량 데이터를 정의한다.
 */
@Generated
@Entity
@Getter
@NoArgsConstructor
public final class Vehicle {
    @Id
    @GeneratedValue
    private Long id;

    private Long userId;

    private Double latitude;

    private Double longitude;

    private boolean isDriving = false;

    public Vehicle(final Long userId) {
        this.userId = userId;
    }

    /**
     * 차량의 운행상태를 변경한다.
     */
    public void toggleDriving() {
        isDriving = !isDriving;
    }

    /**
     * 차량의 위치를 업데이트한다.
     *
     * @param latitude 위도
     * @param longitude 경도
     */
    public void updateLocation(final Double latitude, final Double longitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
