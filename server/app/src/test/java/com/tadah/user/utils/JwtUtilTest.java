package com.tadah.user.utils;

import com.tadah.user.exceptions.InvalidClaimDataException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayName("JwtUtil 클래스")
public final class JwtUtilTest {
    public static final Long CLAIM_DATA = 1L;
    public static final String SECRET = "12345678901234567890123456789012";
    public static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";
    public static final String INVALID_TOKEN = VALID_TOKEN + "invalid";

    private final JwtUtil jwtUtil;
    public JwtUtilTest() {
        jwtUtil = new JwtUtil(SECRET);
    }

    @Nested
    @DisplayName("encode 메서드는")
    public final class Describe_encode {
        private String subject(final Long claimData) {
            return jwtUtil.encode(claimData);
        }

        @Test
        @DisplayName("속성 정보를 인코딩하여 토큰을 리턴한다.")
        public void it_encodes_claim_data_and_then_returns_the_token() {
            assertThat(subject(CLAIM_DATA))
                .isEqualTo(VALID_TOKEN);
        }

        @Nested
        @DisplayName("속성 정보에 null이 들어오는 경우")
        public final class Context_claimDataNull {
            @Test
            @DisplayName("InvalidClaimException을 던진다.")
            public void it_throws_invalid_claim_exception() {
                assertThatThrownBy(() -> subject(null))
                    .isInstanceOf(InvalidClaimDataException.class);
            }
        }
    }
}
