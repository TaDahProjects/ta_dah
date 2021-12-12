package com.tadah.vehicle.applications;

import com.tadah.vehicle.domains.entities.Vehicle;
import com.tadah.vehicle.domains.repositories.VehicleRepository;
import com.tadah.vehicle.exceptions.VehicleAlreadyExistException;
import com.tadah.vehicle.exceptions.VehicleNotDrivingException;
import com.tadah.vehicle.exceptions.VehicleNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 차량 조회, 생성, 운행여부 수정, 위치 정보 업데이트를 담당한다.
 */
@Service
public final class VehicleService {
    private final VehicleRepository vehicleRepository;

    public VehicleService(final VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    private Vehicle findVehicleAndUpdateLocation(final Long userId, final Double latitude, final Double longitude) {
        final Vehicle vehicle = vehicleRepository.findByUserId(userId)
            .orElseThrow(VehicleNotFoundException::new);

        vehicle.updateLocation(latitude, longitude);

        return vehicle;
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

    /**
     * 차량 운행을 시작한다.
     *
     * @param userId 차량의 소유자
     * @param latitude 운행 시작 위도
     * @param longitude 운행 시작 경도
     * @throws VehicleNotFoundException 사용자가 차량을 소유하고 있지 않은 경우
     * @return 운행을 시작한 차량 정보
     */
    public Vehicle startDriving(final Long userId, final Double latitude, final Double longitude) {
        final Vehicle vehicle = findVehicleAndUpdateLocation(userId, latitude, longitude);

        if (!vehicle.isDriving()) {
            vehicle.toggleDriving();
        }

        return vehicleRepository.save(vehicle);
    }

    /**
     * 차량 운행을 종료한다.
     *
     * @param userId 차량의 소유자
     * @param latitude 운행 종료 위도
     * @param longitude 운행 종료 경도
     * @throws VehicleNotFoundException 사용자가 차량을 소유하고 있지 않은 경우
     * @return 운행을 종료한 차량 정보
     */
    public Vehicle stopDriving(final Long userId, final Double latitude, final Double longitude) {
        final Vehicle vehicle = findVehicleAndUpdateLocation(userId, latitude, longitude);

        if (vehicle.isDriving()) {
            vehicle.toggleDriving();
        }

        return vehicleRepository.save(vehicle);
    }

    /**
     * 차량의 위치를 업데이트한다.
     *
     * @param userId 차량의 소유자
     * @param latitude 위도
     * @param longitude 경도
     * @return 위치를 업데이트한 차량 정보
     * @throws VehicleNotFoundException 사용자가 차량을 소유하고 있지 않은 경우
     * @throws VehicleNotDrivingException 차량 운행이 종료된 경우
     */
    public Vehicle updateLocation(final Long userId, final Double latitude, final Double longitude) {
        final Vehicle vehicle = findVehicleAndUpdateLocation(userId, latitude, longitude);

        if (!vehicle.isDriving()) {
            throw new VehicleNotDrivingException();
        }

        return vehicleRepository.save(vehicle);
    }
}
