package com.tadah.vehicle.exceptions;

/**
 * 차량 조회에 실패한경우 던져진다.
 */
public class VehicleNotFoundException extends RuntimeException {
    public VehicleNotFoundException() {
        super("차량 조회에 실패했습니다.");
    }
}
