package com.tadah;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

import static org.mockito.Mockito.mockStatic;

@DisplayName("TaDah 클래스")
public final class TaDahTest {
    @Nested
    @DisplayName("main 메서드는")
    @TestInstance(Lifecycle.PER_CLASS)
    public final class Describe_main {
        private MockedStatic<SpringApplication> springMock;
        private final String[] args = new String[] {};

        @BeforeAll
        private void beforeAll() {
            springMock = mockStatic(SpringApplication.class);
        }

        @BeforeEach
        private void beforeEach() {
            springMock
                .when(() -> SpringApplication.run(TaDah.class, args))
                .thenReturn(null);
        }

        @AfterEach
        private void afterEach() {
            springMock
                .verify(() -> SpringApplication.run(TaDah.class, args));
        }

        @AfterAll
        private void afterAll() {
            springMock.close();
        }

        @Test
        @DisplayName("스프링 어플리케이션을 실행시킨다.")
        public void it_executes_spring_application() {
            TaDah.main(args);
        }
    }
}
