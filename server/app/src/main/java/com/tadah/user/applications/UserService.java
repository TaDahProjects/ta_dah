package com.tadah.user.applications;

import com.tadah.user.domain.entities.User;
import com.tadah.user.domain.repositories.UserRepository;
import com.tadah.user.exceptions.UserEmailDuplicationException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * User 관련 작업을 담당한다.
 */
@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;

    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * User를 저장하고 리턴한다.
     *
     * @param user 저장할 User
     * @return 저장한 User
     */
    public User saveUser(final User user) {
        if (this.userRepository.existsByEmail(user.getEmail())) {
            throw new UserEmailDuplicationException();
        }
        return this.userRepository.save(user);
    }
}
