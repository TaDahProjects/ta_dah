package com.tadah.user.domain.repositories;

import com.tadah.user.domain.entities.User;
import com.tadah.user.domain.repositories.infra.JpaUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@DisplayName("UserRepository 클래스")
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JpaUserRepository jpaUserRepository;

    @BeforeEach
    private void beforeEach() {
        jpaUserRepository.deleteAll();
    }

    @Nested
    @DisplayName("save 메서드는")
    public final class Describe_save {
        private User subject(final User user) {
            return userRepository.save(user);
        }

        @Test
        @DisplayName("사용자 정보를 저장한다.")
        public void it_saves_a_user_data() {
            final User savedUser = subject(new User());

            assertThat(jpaUserRepository.findById(savedUser.getId()))
                .isPresent()
                .get()
                .isInstanceOf(User.class);
        }
    }
}
