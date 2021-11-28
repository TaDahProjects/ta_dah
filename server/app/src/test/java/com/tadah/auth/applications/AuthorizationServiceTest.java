package com.tadah.auth.applications;

import com.tadah.auth.domain.entities.Role;
import com.tadah.auth.domain.repositories.RoleRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.tadah.auth.domain.entities.RoleTest.ROLE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("AuthorizationService 클래스")
public final class AuthorizationServiceTest {
    private final RoleRepository roleRepository;
    private final AuthorizationService authorizationService;

    public AuthorizationServiceTest() {
        this.roleRepository = mock(RoleRepository.class);
        this.authorizationService = new AuthorizationService(roleRepository);
    }

    @Nested
    @DisplayName("create 메서드는")
    public final class Describe_create {
        private Role subject() {
            return authorizationService.create(ROLE);
        }

        @BeforeEach
        private void beforeEach() {
            when(roleRepository.save(any(Role.class)))
                .thenReturn(ROLE);
        }

        @AfterEach
        private void afterEach() {
            verify(roleRepository, atMostOnce())
                .save(any(Role.class));
        }

        @Test
        @DisplayName("권한을 저장한다.")
        public void it_saves_a_role() {
            assertThat(subject())
                .isInstanceOf(Role.class);
        }
    }

    @Nested
    @DisplayName("list 메서드는")
    private final class Describe_list {
        private List<Role> subject() {
            return authorizationService.list(ROLE.getUserId());
        }

        @BeforeEach
        private void beforeEach() {
            when(roleRepository.findAllByUserId(anyLong()))
                .thenReturn(List.of(ROLE));
        }

        @AfterEach
        private void afterEach() {
            verify(roleRepository, atMostOnce())
                .findAllByUserId(anyLong());
        }

        @Test
        @DisplayName("권한 목록을 리턴한다.")
        public void it_returns_a_roles() {
            assertThat(subject())
                .isInstanceOf(List.class);
        }
    }
}
