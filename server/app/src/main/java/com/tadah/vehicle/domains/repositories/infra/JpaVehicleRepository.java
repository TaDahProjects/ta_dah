package com.tadah.vehicle.domains.repositories.infra;

import com.tadah.vehicle.domains.entities.Vehicle;
import com.tadah.vehicle.domains.repositories.VehicleRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaVehicleRepository extends VehicleRepository, JpaRepository<Vehicle, Long> {
}
