package com.tadah.user.domain.repositories;

import com.tadah.user.domain.entities.User;
import com.tadah.user.domain.repositories.infra.JpaUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static com.tadah.user.UserConstants.DRIVER;
import static com.tadah.user.UserConstants.RIDER;
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
            final User rider = subject(RIDER);
            assertThat(jpaUserRepository.findById(rider.getId()))
                .isPresent()
                .get()
                .isInstanceOf(User.class)
                .matches(user -> RIDER.getEmail().equals(user.getEmail()))
                .matches(user -> RIDER.getName().equals(user.getName()))
                .matches(user -> RIDER.getPassword().equals(user.getPassword()))
                .matches(user -> RIDER.getUserType().equals(user.getUserType()));

            final User driver = subject(DRIVER);
            assertThat(jpaUserRepository.findById(driver.getId()))
                .isPresent()
                .get()
                .isInstanceOf(User.class)
                .matches(user -> DRIVER.getEmail().equals(user.getEmail()))
                .matches(user -> DRIVER.getName().equals(user.getName()))
                .matches(user -> DRIVER.getPassword().equals(user.getPassword()))
                .matches(user -> DRIVER.getUserType().equals(user.getUserType()));
        }
    }

    @Nested
    @DisplayName("existsByEmail 메서드는")
    public final class Describe_existsByEmail {
        private boolean subject(final String email) {
            return userRepository.existsByEmail(email);
        }

        @Nested
        @DisplayName("이메일에 해당하는 사용자가 있는 경우")
        public final class Context_userExist {
            @BeforeEach
            private void beforeEach() {
                new Describe_save().subject(RIDER);
            }

            @Test
            @DisplayName("사용자가 존재하는것을 알려준다.")
            public void it_notifies_that_the_user_exists() {
                assertThat(subject(RIDER.getEmail()))
                    .isTrue();
            }
        }

        @Nested
        @DisplayName("이메일에 해당하는 사용자가 없는 경우")
        public final class Context_userNotExist {
            @Test
            @DisplayName("사용자가 존재하지 않는것을 알려준다.")
            public void it_notifies_that_user_does_not_exist() {
                assertThat(subject(RIDER.getEmail()))
                    .isFalse();
            }
        }
    }
}
