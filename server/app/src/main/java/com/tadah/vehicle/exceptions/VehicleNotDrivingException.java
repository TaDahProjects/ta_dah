package com.tadah.vehicle.exceptions;

/**
 * 차량 운행이 종료되었는데 위치를 업데이트하려는경우 던져진다.
 */
public class VehicleNotDrivingException extends RuntimeException {
    public VehicleNotDrivingException() {
        super("차량이 운행중이지 않습니다.");
    }
}
