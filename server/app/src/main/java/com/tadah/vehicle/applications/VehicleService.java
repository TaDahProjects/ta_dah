package com.tadah.vehicle.applications;

import com.tadah.vehicle.domains.entities.Vehicle;
import com.tadah.vehicle.domains.repositories.VehicleRepository;
import org.springframework.stereotype.Service;

/**
 * 차량 생성을 수행한다.
 */
@Service
public final class VehicleService {
    private final VehicleRepository vehicleRepository;

    public VehicleService(final VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    /**
     * 차량 생성을 수행한다.
     *
     * @param vehicle 생성할 차량 정보
     * @return 생성한 차량 정보
     */
    public Vehicle create(final Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }
}
