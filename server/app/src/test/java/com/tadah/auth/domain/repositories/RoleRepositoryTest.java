package com.tadah.auth.domain.repositories;

import com.tadah.auth.domain.entities.Role;
import com.tadah.auth.domain.repositories.infra.JpaRoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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

    @BeforeEach
    private void beforeEach() {
        jpaRoleRepository.deleteAll();
    }

    @Nested
    @DisplayName("save 메서드는")
    public final class  Describe_save {
        private Role subject(final Role role) {
            return roleRepository.save(role);
        }

        @Test
        @DisplayName("권한을 저장한다.")
        public void it_saves_a_role_data() {
            final Role savedRole = subject(ROLE);

            assertThat(jpaRoleRepository.findById(savedRole.getId()))
                .isPresent()
                .get()
                .matches(role -> ROLE.getName().equals(role.getName()))
                .matches(role -> ROLE.getUserId().equals(role.getUserId()));
        }
    }
}
