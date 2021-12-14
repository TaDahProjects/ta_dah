package com.tadah.user.applications;

import com.tadah.user.domains.entities.User;
import com.tadah.user.domains.repositories.UserRepository;
import com.tadah.user.domains.repositories.infra.JpaUserRepository;
import com.tadah.user.exceptions.UserEmailAlreadyExistException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.tadah.user.domains.entities.UserTest.EMAIL;
import static com.tadah.user.domains.entities.UserTest.NAME;
import static com.tadah.user.domains.entities.UserTest.PASSWORD;
import static com.tadah.user.domains.repositories.UserRepositoryTest.USER_WITHOUT_PASSWORD;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DataJpaTest
@DisplayName("UserService 클래스")
public class UserServiceTest {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private JpaUserRepository jpaUserRepository;

    public UserServiceTest(
        @Autowired final UserRepository userRepository,
        @Autowired final PasswordEncoder passwordEncoder
    ) {
        this.passwordEncoder = passwordEncoder;
        this.userService = new UserService(userRepository, passwordEncoder);
    }

    @AfterEach
    private void afterEach() {
        jpaUserRepository.deleteAll();
    }

    @Nested
    @DisplayName("register 메서드는")
    public final class Describe_register {
        private User subject() {
            return userService.register(USER_WITHOUT_PASSWORD, PASSWORD);
        }

        @Nested
        @DisplayName("이메일에 해당하는 사용자가 없는경우")
        public final class Context_validEmail {
            @Test
            @DisplayName("사용자를 등록한다.")
            public void it_registers_users() {
                assertThat(subject())
                    .isInstanceOf(User.class)
                    .matches(user -> EMAIL.equals(user.getEmail()))
                    .matches(user -> NAME.equals(user.getName()))
                    .matches(user -> passwordEncoder.matches(PASSWORD, user.getPassword()));
            }
        }

        @Nested
        @DisplayName("이메일에 해당하는 사용자가 있는 경우")
        public final class Context_invalidEmail {
            @BeforeEach
            private void beforeEach() {
                subject();
            }

            @Test
            @DisplayName("UserEmailDuplicationException을 던진다.")
            public void it_throws_user_email_duplication_exception() {
                assertThatThrownBy(Describe_register.this::subject)
                    .isInstanceOf(UserEmailAlreadyExistException.class);
            }
        }
    }
}
