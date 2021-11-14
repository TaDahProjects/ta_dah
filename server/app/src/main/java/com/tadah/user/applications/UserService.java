package com.tadah.user.applications;

import com.tadah.user.domain.entities.User;
import com.tadah.user.domain.repositories.UserRepository;
import com.tadah.user.exceptions.UserEmailAlreadyExistException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * 사용자 등록을 수행한다.
 */
@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(final UserRepository userRepository, final PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 사용자 등록을 수행한다.
     *
     * @param user 비밀번호를 제외한 사용자 정보
     * @param password 사용자 비밀번호
     * @throws UserEmailAlreadyExistException 이미 존재하는 사용자 이메일인 경우
     * @return 등록한 사용자 정보
     */
    public User registerUser(final User user, final String password) {
        if (this.userRepository.existsByEmail(user.getEmail())) {
            throw new UserEmailAlreadyExistException();
        }
        user.updatePassword(password, passwordEncoder);
        return this.userRepository.save(user);
    }
}
