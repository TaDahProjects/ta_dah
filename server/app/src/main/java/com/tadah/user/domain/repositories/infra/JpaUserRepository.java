package com.tadah.user.domain.repositories.infra;

import com.tadah.user.domain.entities.User;
import com.tadah.user.domain.repositories.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserRepository extends UserRepository, JpaRepository<User, Long> {
}