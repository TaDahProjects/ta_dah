package com.tadah.user.utils;

import com.tadah.user.exceptions.InvalidClaimDataException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;

/**
 * JWT 인코딩 디코딩을 수행한다.
 */
@Component
public final class JwtUtil {
    public static final String CLAIM_NAME = "userId";

    private final Key key;
    public JwtUtil(@Value("${jwt.secret}") final String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 속성 정보를 인코딩하여 토큰을 리턴한다.
     *
     * @param claimData JWT Claim(속성 정보)에 들어갈 데이터
     * @return JWT
     * @throws InvalidClaimDataException claimData에 null값이 들어온 경우
     */
    public String encode(final Long claimData) {
        if (claimData == null) {
            throw new InvalidClaimDataException();
        }
        return Jwts.builder()
            .claim(CLAIM_NAME, claimData)
            .signWith(key)
            .compact();
    }
}
