package com.tadah.user.domain.repositories;

import com.tadah.user.domain.entities.User;

public interface UserRepository {
    User save(final User user);
}
