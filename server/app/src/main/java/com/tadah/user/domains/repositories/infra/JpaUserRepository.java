package com.tadah.user.domains.repositories.infra;

import com.tadah.user.domains.entities.User;
import com.tadah.user.domains.repositories.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserRepository extends UserRepository, JpaRepository<User, Long> {
}
