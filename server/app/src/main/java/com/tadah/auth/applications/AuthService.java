package com.tadah.auth.applications;

import com.tadah.auth.utils.JwtUtil;
import com.tadah.common.exceptions.InvalidTokenException;
import com.tadah.user.domain.entities.User;
import com.tadah.user.domain.repositories.UserRepository;
import com.tadah.user.exceptions.LoginFailException;
import io.jsonwebtoken.Claims;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.tadah.auth.utils.JwtUtil.CLAIM_NAME;

/**
 * 인증 및 인가를 담당한다.
 */
@Service
public final class AuthService {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
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
    public String publishToken(final String email, final String password) {
        final User user = userRepository.findByEmail(email)
            .orElseThrow(LoginFailException::new);

        if (!user.authenticate(password, passwordEncoder)) {
            throw new LoginFailException();
        }
        return jwtUtil.encode(user.getId());
    }

    /**
     * JWT에 해당하는 사용자 데이터를 리턴한다.
     *
     * @param token JWT
     * @return 사용자 데이터
     * @throws InvalidTokenException 토큰 검증에 실패한 경우
     */
    public User verifyToken(final String token) {
        final Claims claims = jwtUtil.decode(token);

        final Long userId = claims.get(CLAIM_NAME, Long.class);
        if (userId == null) {
            throw new InvalidTokenException();
        }
        return userRepository.findById(userId).orElseThrow(InvalidTokenException::new);
    }
}
