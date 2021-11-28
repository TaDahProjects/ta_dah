package com.tadah.auth.domain.repositories;

import com.tadah.auth.domain.entities.Role;
import com.tadah.auth.domain.repositories.infra.JpaRoleRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static com.tadah.auth.domain.entities.RoleTest.ROLE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class RoleRepositoryTest {
    private final RoleRepository roleRepository;
    private final JpaRoleRepository jpaRoleRepository;

    public RoleRepositoryTest(
        @Autowired final RoleRepository roleRepository,
        @Autowired final JpaRoleRepository jpaRoleRepository
    ) {
        this.roleRepository = roleRepository;
        this.jpaRoleRepository = jpaRoleRepository;
    }

    @AfterEach
    private void afterEach() {
        jpaRoleRepository.deleteAll();
    }

    @Nested
    @DisplayName("save 메서드는")
    public final class  Describe_save {
        private Role subject() {
            return roleRepository.save(ROLE);
        }

        @Test
        @DisplayName("권한을 저장한다.")
        public void it_saves_a_role_data() {
            final Role savedRole = subject();

            assertThat(jpaRoleRepository.findById(savedRole.getId()))
                .isPresent()
                .get()
                .matches(role -> ROLE.getName().equals(role.getName()))
                .matches(role -> ROLE.getUserId().equals(role.getUserId()));
        }
    }

    @Nested
    @DisplayName("findByUserId 메서드는")
    public final class Describe_findByUserId {
        private List<Role> subject() {
            return roleRepository.findAllByUserId(ROLE.getUserId());
        }

        @BeforeEach
        private void beforeEach() {
            new Describe_save().subject();
        }

        @Test
        @DisplayName("권한을 리턴한다.")
        public void it_returns_a_role_data() {
            final List<Role> roles = subject();

            assertThat(roles.size())
                .isEqualTo(1);

            assertThat(roles.get(0))
                .matches(role -> ROLE.getName().equals(role.getName()))
                .matches(role -> ROLE.getUserId().equals(role.getUserId()));
        }

        @Nested
        @DisplayName("권한이 없는 경우")
        public final class Context_emptyRole {
            @BeforeEach
            private void beforeEach() {
                jpaRoleRepository.deleteAll();
            }

            @Test
            @DisplayName("권한이 없음을 알려준다.")
            public void it_notifies_that_role_is_empty() {
                assertThat(subject().size())
                    .isEqualTo(0);
            }
        }
    }
}
