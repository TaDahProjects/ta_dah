package com.tadah.vehicle.applications;

import com.tadah.vehicle.dtos.DrivingDataProto;
import com.tadah.vehicle.exceptions.SendMessageFailException;
import com.tadah.vehicle.utils.KinesisProducer;
import org.springframework.stereotype.Service;

/**
 * 차량 조회, 생성, 운행여부 수정, 위치 정보 업데이트를 담당한다.
 */
@Service
public final class VehicleService {
    private final KinesisProducer kinesisProducer;

    public VehicleService(final KinesisProducer kinesisProducer) {
        this.kinesisProducer = kinesisProducer;
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

        if (!kinesisProducer.sendData(drivingData)) {
            throw new SendMessageFailException();
        }
    }

    /**
     * 차량의 운행정보를 업데이트한다
     *
     * @param userId 차량의 소유자
     * @param latitude 위도
     * @param longitude 경도
     * @throws SendMessageFailException 메시지 전송이 실패한 경우
     */
    public void updateDriving(final Long userId, final Double latitude, final Double longitude) {
        final DrivingDataProto.DrivingData drivingData = DrivingDataProto.DrivingData.newBuilder()
            .setUserId(userId)
            .setLatitude(latitude)
            .setLongitude(longitude)
            .setDrivingStatus(DrivingDataProto.DrivingStatus.DRIVING)
            .build();

        if (!kinesisProducer.sendData(drivingData)) {
            throw new SendMessageFailException();
        }
    }

    /**
     * 차량 운행을 종료한다.
     *
     * @param userId 차량의 소유자
     * @param latitude 운행 종료 위도
     * @param longitude 운행 종료 경도
     * @throws SendMessageFailException 메시지 전송이 실패한 경우
     */
    public void stopDriving(final Long userId, final Double latitude, final Double longitude) {
        final DrivingDataProto.DrivingData drivingData = DrivingDataProto.DrivingData.newBuilder()
            .setUserId(userId)
            .setLatitude(latitude)
            .setLongitude(longitude)
            .setDrivingStatus(DrivingDataProto.DrivingStatus.STOP)
            .build();

        if (!kinesisProducer.sendData(drivingData)) {
            throw new SendMessageFailException();
        }
    }
}
