package com.tadah.user.applications;

import com.tadah.user.domain.entities.User;
import com.tadah.user.domain.repositories.UserRepository;
import com.tadah.user.exceptions.LoginFailException;
import com.tadah.user.utils.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 로그인을 담당한다.
 */
@Service
public final class AuthenticationService {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(
        final JwtUtil jwtUtil, final UserRepository userRepository, final PasswordEncoder passwordEncoder
    ) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 사용자를 확인하고, JWT를 발행한다.
     *
     * @param email 사용자 이메일
     * @param password 사용자 비밀번호
     * @return JWT
     * @throws LoginFailException 로그인에 실패한 경우
     */
    public String login(final String email, final String password) {
        final User user = userRepository.findByEmail(email)
            .orElseThrow(LoginFailException::new);

        if (!user.authenticate(password, passwordEncoder)) {
            throw new LoginFailException();
        }
        return jwtUtil.encode(user.getId());
    }
}
