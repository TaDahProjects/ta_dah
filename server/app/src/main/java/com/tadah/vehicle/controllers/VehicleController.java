package com.tadah.vehicle.controllers;

import com.tadah.user.domains.entities.User;
import com.tadah.vehicle.applications.VehicleService;
import com.tadah.vehicle.domains.entities.Vehicle;
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
    private final VehicleService vehicleService;

    public VehicleController(final VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    private Vehicle toVehicle(final User user) {
        return new Vehicle(user.getId());
    }

    /**
     * 차량 생성을 수행한다.
     * @param user 생성할 차량의 소유자
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("isAuthenticated()")
    public void create(@AuthenticationPrincipal final User user) {
        final Vehicle vehicle = toVehicle(user);
        vehicleService.create(vehicle);
    }
}
