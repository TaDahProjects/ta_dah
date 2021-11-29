package com.tadah.user.domains.repositories;

import com.tadah.user.domains.entities.User;
import com.tadah.user.domains.repositories.infra.JpaUserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.TransactionSystemException;

import java.util.Optional;

import static com.tadah.user.domains.entities.UserTest.EMAIL;
import static com.tadah.user.domains.entities.UserTest.NAME;
import static com.tadah.user.domains.entities.UserTest.PASSWORD;
import static com.tadah.user.domains.entities.UserTest.USER;
import static com.tadah.user.domains.entities.UserTest.USER_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DataJpaTest
@DisplayName("UserRepository 클래스")
public class UserRepositoryTest {
    private static final User USER_WITHOUT_PASSWORD = new User(EMAIL, NAME);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JpaUserRepository jpaUserRepository;

    @AfterEach
    private void afterEach() {
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
                assertThatThrownBy(() -> subject(USER_WITHOUT_PASSWORD))
                    .isInstanceOf(TransactionSystemException.class);
            }
        }

        @Test
        @DisplayName("사용자 정보를 저장한다.")
        public void it_saves_a_user_data() {
            final User savedUser = subject(USER);
            assertThat(new Describe_findById().subject(savedUser.getId()))
                .isPresent()
                .get()
                .isInstanceOf(User.class)
                .matches(user -> EMAIL.equals(user.getEmail()))
                .matches(user -> NAME.equals(user.getName()))
                .matches(user -> passwordEncoder.matches(PASSWORD, user.getPassword()));
        }
    }

    @Nested
    @DisplayName("existsByEmail 메서드는")
    public final class Describe_existsByEmail {
        private boolean subject() {
            return userRepository.existsByEmail(EMAIL);
        }

        @Nested
        @DisplayName("이메일에 해당하는 사용자가 있는 경우")
        public final class Context_userExist {
            @BeforeEach
            private void beforeEach() {
                new Describe_save().subject(USER);
            }

            @Test
            @DisplayName("사용자가 존재하는것을 알려준다.")
            public void it_notifies_that_the_user_exists() {
                assertThat(subject())
                    .isTrue();
            }
        }

        @Nested
        @DisplayName("이메일에 해당하는 사용자가 없는 경우")
        public final class Context_userNotExist {
            @Test
            @DisplayName("사용자가 존재하지 않는것을 알려준다.")
            public void it_notifies_that_user_does_not_exist() {
                assertThat(subject())
                    .isFalse();
            }
        }
    }

    @Nested
    @DisplayName("findByEmail 메서드는")
    public final class Describe_findByEmail {
        private Optional<User> subject() {
            return userRepository.findByEmail(EMAIL);
        }

        @Nested
        @DisplayName("이메일에 해당하는 사용자가 없는 경우")
        public final class Context_userNotExist {
            @Test
            @DisplayName("사용자가 존재하지 않는것을 알려준다.")
            public void it_notifies_that_user_does_not_exist() {
                assertThat(subject())
                    .isEmpty();
            }
        }

        @Nested
        @DisplayName("이메일에 해당하는 사용자가 있는 경우")
        public final class Context_userExist {
            @BeforeEach
            private void beforeEach() {
                new Describe_save().subject(USER);
            }

            @Test
            @DisplayName("사용자가 존재하는것을 알려준다.")
            public void it_notifies_that_the_user_exists() {
                assertThat(subject())
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
                assertThat(subject(USER_ID))
                    .isEmpty();
            }
        }

        @Nested
        @DisplayName("id에 해당하는 사용자가 있는 경우")
        public final class Context_userExist {
            private Long userId;

            @BeforeEach
            private void beforeEach() {
                userId = new Describe_save().subject(USER).getId();
            }

            @Test
            @DisplayName("사용자가 존재하는것을 알려준다.")
            public void it_notifies_that_the_user_exists() {
                assertThat(subject(userId))
                    .isPresent();
            }
        }
    }
}
