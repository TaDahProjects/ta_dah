package com.tadah.vehicle.exceptions;

/**
 * 사용자의 차량이 이미 등록되어있는 경우 던져진다.
 */
public final class VehicleAlreadyExistException extends RuntimeException {
    public VehicleAlreadyExistException() {
        super("차량이 이미 존재합니다.");
    }
}
