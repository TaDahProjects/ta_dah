package com.tadah.vehicle.domains.entities;

import com.tadah.common.annotations.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Generated
@Entity
@Getter
@NoArgsConstructor
public final class Vehicle {
    @Id
    @GeneratedValue
    private Long id;

    private Long userId;

    @Setter
    private Double latitude;

    @Setter
    private Double longitude;

    private boolean isRunning = false;

    public Vehicle(final Long userId) {
        this.userId = userId;
    }
}
