package com.tadah.auth.applications;

import com.tadah.auth.domains.entities.Role;
import com.tadah.auth.domains.repositories.RoleRepository;
import com.tadah.auth.domains.repositories.infra.JpaRoleRepository;
import com.tadah.user.domains.repositories.infra.JpaUserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static com.tadah.auth.domains.entities.RoleTest.ROLE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@DataJpaTest
@DisplayName("AuthorizationService 클래스")
public class AuthorizationServiceTest {
    @Autowired
    private JpaRoleRepository jpaRoleRepository;

    @Autowired
    private JpaUserRepository jpaUserRepository;

    private final RoleRepository roleRepository;
    private final AuthorizationService authorizationService;
    public AuthorizationServiceTest(@Autowired final RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
        this.authorizationService = new AuthorizationService(roleRepository);
    }

    @AfterEach
    private void afterEach() {
        jpaUserRepository.deleteAll();
        jpaRoleRepository.deleteAll();
    }

    @Nested
    @DisplayName("create 메서드는")
    public final class Describe_create {
        private Role subject() {
            return authorizationService.create(ROLE);
        }

        @Test
        @DisplayName("권한을 저장한다.")
        public void it_saves_a_role() {
            assertThat(subject())
                .matches(role -> ROLE.getName().equals(role.getName()))
                .matches(role -> ROLE.getUserId().equals(role.getUserId()));
        }
    }

    @Nested
    @DisplayName("list 메서드는")
    public final class Describe_list {
        private List<Role> subject() {
            return authorizationService.list(ROLE.getUserId());
        }

        @BeforeEach
        private void beforeEach() {
            roleRepository.save(ROLE);
        }

        @Test
        @DisplayName("권한 목록을 리턴한다.")
        public void it_returns_a_roles() {
            final List<Role> roles = subject();

            assertThat(roles.size())
                .isEqualTo(1);

            assertThat(roles.get(0))
                .matches(role -> ROLE.getName().equals(role.getName()))
                .matches(role -> ROLE.getUserId().equals(role.getUserId()));
        }
    }
}
