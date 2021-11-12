package com.tadah.user.applications;

import com.tadah.user.domain.entities.User;
import com.tadah.user.domain.repositories.UserRepository;
import com.tadah.user.exceptions.UserEmailAlreadyExistException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * 사용자 등록을 수행한다.
 */
@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;

    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 사용자 등록을 수행한다.
     *
     * @param user 등록할 사용자 정보
     * @return 등록한 사용자 정보
     * @throws UserEmailAlreadyExistException 이미 존재하는 사용자 이메일인 경우
     */
    public User registerUser(final User user) {
        if (this.userRepository.existsByEmail(user.getEmail())) {
            throw new UserEmailAlreadyExistException();
        }
        return this.userRepository.save(user);
    }
}
