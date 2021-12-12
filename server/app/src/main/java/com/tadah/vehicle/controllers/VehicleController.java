package com.tadah.vehicle.controllers;

import com.tadah.auth.applications.AuthorizationService;
import com.tadah.auth.domains.entities.Role;
import com.tadah.user.domains.entities.User;
import com.tadah.vehicle.applications.VehicleService;
import com.tadah.vehicle.domains.entities.Vehicle;
import com.tadah.vehicle.dtos.DrivingRequestData;
import com.tadah.vehicle.exceptions.VehicleAlreadyExistException;
import com.tadah.vehicle.exceptions.VehicleNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 차량 생성, 운행여부 수정, 위치 업데이트를 담당한다.
 */
@CrossOrigin
@RestController
@RequestMapping("/vehicles")
public class VehicleController {
    private static final String DRIVER_ROLE = "DRIVER";

    private final VehicleService vehicleService;
    private final AuthorizationService authorizationService;

    public VehicleController(
        final VehicleService vehicleService,
        final AuthorizationService authorizationService
    ) {
        this.vehicleService = vehicleService;
        this.authorizationService = authorizationService;
    }

    private Vehicle toEntity(final User user) {
        return new Vehicle(user.getId());
    }

    /**
     * 차량 생성을 수행한다.
     * @throws VehicleAlreadyExistException 차량이 이미 존재하는 경우
     * @param user 생성할 차량의 소유자
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("isAuthenticated()")
    public void create(@AuthenticationPrincipal final User user) {
        final Vehicle vehicle = toEntity(user);
        authorizationService.create(new Role(user.getId(), DRIVER_ROLE));
        vehicleService.create(vehicle);
    }

    /**
     * 차량 운행을 시작한다.
     *
     * @param user 차량의 소유자
     * @param drivingRequestData 차량 운행 시작 위치 정보
     * @throws VehicleNotFoundException 사용자가 차량을 소유하고 있지 않은 경우
     */
    @PostMapping("/driving")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("isAuthenticated() and hasAuthority('DRIVER')")
    public void startDriving(
        @AuthenticationPrincipal final User user,
        @RequestBody @Valid final DrivingRequestData drivingRequestData
    ) {
        final Long userId = user.getId();
        final Double latitude = drivingRequestData.getLatitude();
        final Double longitude = drivingRequestData.getLongitude();
        vehicleService.startDriving(userId, latitude, longitude);
    }

    /**
     * 차량 운행을 종료한다.
     *
     * @param user 차량의 소유자
     * @param drivingRequestData 차량 운행 종료 위치 정보
     * @throws VehicleNotFoundException 사용자가 차량을 소유하고 있지 않은 경우
     */
    @DeleteMapping("/driving")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("isAuthenticated() and hasAuthority('DRIVER')")
    public void stopDriving(
        @AuthenticationPrincipal final User user,
        @RequestBody @Valid final DrivingRequestData drivingRequestData
    ) {
        final Long userId = user.getId();
        final Double latitude = drivingRequestData.getLatitude();
        final Double longitude = drivingRequestData.getLongitude();
        vehicleService.stopDriving(userId, latitude, longitude);
    }
}
