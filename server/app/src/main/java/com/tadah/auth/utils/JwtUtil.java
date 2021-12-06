package com.tadah.auth.utils;

import com.tadah.auth.exceptions.InvalidTokenException;
import io.jsonwebtoken.Claims;
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
    public JwtUtil(@Value("${spring.jwt.secret}") final String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 속성 정보를 인코딩하여 토큰을 리턴한다.
     *
     * @param claimData JWT Claim(속성 정보)에 들어갈 데이터
     * @return JWT
     */
    public String encode(final Long claimData) {
        return Jwts.builder()
            .claim(CLAIM_NAME, claimData)
            .signWith(key)
            .compact();
    }

    /**
     * 토큰을 디코딩하여 속성 정보를 리턴한다.
     *
     * @param token JWT
     * @return JWT Claim(속성 정보)
     * @throws InvalidTokenException 토큰이 유효하지 않은 경우
     */
    public Claims decode(final String token) {
        try {
            return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        } catch (Exception exception) {
            throw new InvalidTokenException();
        }
    }
}
