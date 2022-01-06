package com.tadah.driving.domains.repositories.infra;

import com.tadah.driving.domains.entities.Driving;
import com.tadah.driving.domains.repositories.DrivingRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaDrivingRepository extends DrivingRepository, JpaRepository<Driving, Long> {
}
