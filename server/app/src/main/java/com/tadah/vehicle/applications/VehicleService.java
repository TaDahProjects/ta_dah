package com.tadah.vehicle.applications;

import com.tadah.vehicle.domains.entities.Vehicle;
import com.tadah.vehicle.domains.repositories.VehicleRepository;
import com.tadah.vehicle.exceptions.VehicleAlreadyExistException;
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
     * @throws VehicleAlreadyExistException 차량이 이미 존재하는 경우
     * @return 생성한 차량 정보
     */
    public Vehicle create(final Vehicle vehicle) {
        if (vehicleRepository.existsByUserId(vehicle.getUserId())) {
            throw new VehicleAlreadyExistException();
        }
        return vehicleRepository.save(vehicle);
    }
}
