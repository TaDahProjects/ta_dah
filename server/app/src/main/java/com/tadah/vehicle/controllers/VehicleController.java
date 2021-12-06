package com.tadah.vehicle.controllers;

import com.tadah.auth.applications.AuthorizationService;
import com.tadah.auth.domains.entities.Role;
import com.tadah.user.domains.entities.User;
import com.tadah.vehicle.applications.VehicleService;
import com.tadah.vehicle.domains.entities.Vehicle;
import com.tadah.vehicle.exceptions.VehicleAlreadyExistException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * 차량 생성을 수행한다.
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
}
