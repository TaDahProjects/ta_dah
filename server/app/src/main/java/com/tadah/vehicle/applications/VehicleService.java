package com.tadah.vehicle.applications;

import com.tadah.vehicle.dtos.DrivingDataProto;
import com.tadah.vehicle.domains.entities.Vehicle;
import com.tadah.vehicle.domains.repositories.VehicleRepository;
import com.tadah.vehicle.exceptions.SendMessageFailException;
import com.tadah.vehicle.exceptions.VehicleNotDrivingException;
import com.tadah.vehicle.exceptions.VehicleNotFoundException;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;
import java.util.function.Supplier;

/**
 * 차량 조회, 생성, 운행여부 수정, 위치 정보 업데이트를 담당한다.
 */
@Service
public final class VehicleService {
    private final VehicleRepository vehicleRepository;
    private final BlockingQueue<DrivingDataProto.DrivingData> blockingQueue;

    public VehicleService(
        final VehicleRepository vehicleRepository,
        final BlockingQueue<DrivingDataProto.DrivingData> blockingQueue
    ) {
        this.blockingQueue = blockingQueue;
        this.vehicleRepository = vehicleRepository;
    }

    private Vehicle findVehicleAndUpdateLocation(final Long userId, final Double latitude, final Double longitude) {
        final Vehicle vehicle = vehicleRepository.findByUserId(userId)
            .orElseThrow(VehicleNotFoundException::new);

        vehicle.updateLocation(latitude, longitude);

        return vehicle;
    }

    private boolean sendData(final DrivingDataProto.DrivingData drivingData) {
        return this.blockingQueue.offer(drivingData);
    }

    /**
     * AWS Kinesis Data Stream Producer를 등록한다
     *
     * @return AWS Kinesis Data Stream Producer
     */
    @Bean
    public Supplier<DrivingDataProto.DrivingData> produceDriving() {
        return this.blockingQueue::poll;
    }

    /**
     * 차량 운행을 시작한다.
     *
     * @param userId 차량의 소유자
     * @param latitude 운행 시작 위도
     * @param longitude 운행 시작 경도
     * @throws SendMessageFailException 메시지 전송이 실패한 경우
     */
    public void startDriving(final Long userId, final Double latitude, final Double longitude) {
        final DrivingDataProto.DrivingData drivingData = DrivingDataProto.DrivingData.newBuilder()
            .setUserId(userId)
            .setLatitude(latitude)
            .setLongitude(longitude)
            .setDrivingStatus(DrivingDataProto.DrivingStatus.START)
            .build();

        if (!sendData(drivingData)) {
            throw new SendMessageFailException();
        }
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
