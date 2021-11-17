package com.tadah.user.domain.repositories;

import com.tadah.user.domain.UserType;
import com.tadah.user.domain.entities.User;
import com.tadah.user.domain.repositories.infra.JpaUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.TransactionSystemException;

import java.util.Optional;

import static com.tadah.user.UserConstants.DRIVER;
import static com.tadah.user.UserConstants.DRIVER_WITH_OUT_PASSWORD;
import static com.tadah.user.UserConstants.EMAIL;
import static com.tadah.user.UserConstants.NAME;
import static com.tadah.user.UserConstants.PASSWORD;
import static com.tadah.user.UserConstants.RIDER;
import static com.tadah.user.UserConstants.RIDER_WITH_OUT_PASSWORD;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DataJpaTest
@DisplayName("UserRepository 클래스")
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JpaUserRepository jpaUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

        @Nested
        @DisplayName("비밀번호가 없는경우")
        public final class Context_passwordNull {
            @Test
            @DisplayName("사용자 정보를 저장하지 않는다.")
            public void it_does_not_save_user_data() {
                assertThatThrownBy(() -> subject(RIDER_WITH_OUT_PASSWORD))
                    .isInstanceOf(TransactionSystemException.class);
                assertThatThrownBy(() -> subject(DRIVER_WITH_OUT_PASSWORD))
                    .isInstanceOf(TransactionSystemException.class);
            }
        }

        @Test
        @DisplayName("사용자 정보를 저장한다.")
        public void it_saves_a_user_data() {
            final User rider = subject(RIDER);
            assertThat(new Describe_findById().subject(rider.getId()))
                .isPresent()
                .get()
                .isInstanceOf(User.class)
                .matches(user -> EMAIL.equals(user.getEmail()))
                .matches(user -> NAME.equals(user.getName()))
                .matches(user -> UserType.RIDER.equals(user.getUserType()))
                .matches(user -> passwordEncoder.matches(PASSWORD, user.getPassword()));

            final User driver = subject(DRIVER);
            assertThat(new Describe_findById().subject(driver.getId()))
                .isPresent()
                .get()
                .isInstanceOf(User.class)
                .matches(user -> EMAIL.equals(user.getEmail()))
                .matches(user -> NAME.equals(user.getName()))
                .matches(user -> UserType.DRIVER.equals(user.getUserType()))
                .matches(user -> passwordEncoder.matches(PASSWORD, user.getPassword()));
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

    @Nested
    @DisplayName("findByEmail 메서드는")
    public final class Describe_findByEmail {
        private Optional<User> subject(final String email) {
            return userRepository.findByEmail(email);
        }

        @Nested
        @DisplayName("이메일에 해당하는 사용자가 없는 경우")
        public final class Context_userNotExist {
            @Test
            @DisplayName("사용자가 존재하지 않는것을 알려준다.")
            public void it_notifies_that_user_does_not_exist() {
                assertThat(subject(RIDER.getEmail()))
                    .isEmpty();
            }
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
                    .isPresent();
            }
        }
    }

    @Nested
    @DisplayName("findById 메서드는")
    public final class Describe_findById {
        private Optional<User> subject(final Long id) {
            return userRepository.findById(id);
        }

        @Nested
        @DisplayName("이메일에 해당하는 사용자가 없는 경우")
        public final class Context_userNotExist {
            @Test
            @DisplayName("사용자가 존재하지 않는것을 알려준다.")
            public void it_notifies_that_user_does_not_exist() {
                assertThat(subject(RIDER.getId()))
                    .isEmpty();
            }
        }

        @Nested
        @DisplayName("id에 해당하는 사용자가 있는 경우")
        public final class Context_userExist {
            @BeforeEach
            private void beforeEach() {
                new Describe_save().subject(RIDER);
            }

            @Test
            @DisplayName("사용자가 존재하는것을 알려준다.")
            public void it_notifies_that_the_user_exists() {
                assertThat(subject(RIDER.getId()))
                    .isPresent();
            }
        }
    }
}
