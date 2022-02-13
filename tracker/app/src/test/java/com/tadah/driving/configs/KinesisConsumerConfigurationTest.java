package com.tadah.driving.configs;

import com.tadah.driving.applications.DrivingService;
import com.tadah.driving.domains.entities.Driving;
import com.tadah.driving.domains.repositories.infra.JpaDrivingRepository;
import com.tadah.driving.dtos.DrivingDataProto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.function.Consumer;
import java.util.stream.Stream;

import static com.tadah.driving.domains.entities.DrivingTest.AFTER_MAP_MATCH;
import static com.tadah.driving.domains.entities.DrivingTest.LATITUDE;
import static com.tadah.driving.domains.entities.DrivingTest.LONGITUDE;
import static com.tadah.driving.domains.entities.DrivingTest.POINT;
import static com.tadah.driving.domains.entities.DrivingTest.USER_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("KinesisConsumerConfiguration 클래스")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class KinesisConsumerConfigurationTest {
    private static final Driving DRIVING = new Driving(USER_ID, POINT);
    private static final DrivingDataProto.DrivingData START_REQUEST = DrivingDataProto.DrivingData.newBuilder()
        .setUserId(USER_ID)
        .setLatitude(LATITUDE)
        .setLongitude(LONGITUDE)
        .setDrivingStatus(DrivingDataProto.DrivingStatus.START)
        .build();
    private static final DrivingDataProto.DrivingData UPDATE_REQUEST = DrivingDataProto.DrivingData.newBuilder()
        .setUserId(USER_ID)
        .setLatitude(LATITUDE)
        .setLongitude(LONGITUDE)
        .setDrivingStatus(DrivingDataProto.DrivingStatus.DRIVING)
        .build();
    private static final DrivingDataProto.DrivingData STOP_REQUEST = DrivingDataProto.DrivingData.newBuilder()
        .setUserId(USER_ID)
        .setLatitude(LATITUDE)
        .setLongitude(LONGITUDE)
        .setDrivingStatus(DrivingDataProto.DrivingStatus.STOP)
        .build();

    @Autowired
    private DrivingService drivingService;

    @Autowired
    private JpaDrivingRepository jpaDrivingRepository;

    @SpyBean
    private KinesisConsumerConfiguration kinesisConsumerConfiguration;

    private Consumer<DrivingDataProto.DrivingData> kinesisConsumer;

    @BeforeAll
    private void beforeAll() {
        kinesisConsumer = kinesisConsumerConfiguration.processDriving();
    }

    @AfterEach
    private void afterEach() {
        jpaDrivingRepository.deleteAll();
    }

    @Nested
    @DisplayName("processDriving 메서드는")
    public final class Describe_processDriving {
        private void subject(final DrivingDataProto.DrivingData drivingData) {
            kinesisConsumer.accept(drivingData);
        }

        @Nested
        @DisplayName("드라이버가 운행을 시작하지 않은 경우")
        public final class Context_stopDriving {
            @Nested
            @DisplayName("운행 시작 요청이 들어오지 않으면")
            public final class Context_invalidDrivingRequest {
                @Test
                @DisplayName("요청이 잘못되었음을 알려준다.")
                public void it_notifies_that_the_driving_request_is_invalid() {
                }
            }

            @Nested
            @DisplayName("운행 시작 요청이 들어오면")
            public final class Context_validDrivingRequest {
                @Test
                @DisplayName("운행을 시작한다")
                public void it_starts_the_driving() {
                    subject(START_REQUEST);

                    assertThat(drivingService.get(USER_ID))
                        .isPresent()
                        .get()
                        .matches(Driving::isDriving)
                        .matches(driving -> driving.getPath().getEndPosition().equals(AFTER_MAP_MATCH.getPosition()));
                }
            }
        }

        @Nested
        @DisplayName("드라이버가 운행을 시작한 경우")
        public final class Context_startDriving {
            @Nested
            @DisplayName("운행 시작 요청이 들어오면")
            public final class Context_invalidDrivingRequest {
                @Test
                @DisplayName("요청이 잘못되었음을 알려준다.")
                public void it_notifies_that_the_driving_request_is_invalid() {

                }
            }

            @Nested
            @TestInstance(TestInstance.Lifecycle.PER_CLASS)
            @DisplayName("운행 종료 또는 운행 정보 업데이트 요청이 들어오면")
            public final class Context_validDrivingRequest {
                private Long id;

                @BeforeEach
                private void beforeEach() {
                    id = drivingService.start(DRIVING).getId();
                }

                private Stream<Arguments> methodSource() {
                    return Stream.of(
                        Arguments.of(true, UPDATE_REQUEST),
                        Arguments.of(false, STOP_REQUEST));
                }

                @MethodSource("methodSource")
                @DisplayName("운행정보를 업데이트한다.")
                @ParameterizedTest(name = "input=\"{1}\"")
                public void it_updates_the_driving_data(final boolean isDriving, final DrivingDataProto.DrivingData drivingData) {
                    subject(drivingData);

                    assertThat(jpaDrivingRepository.findById(id))
                        .isPresent()
                        .get()
                        .matches(driving -> driving.isDriving() == isDriving)
                        .matches(driving -> !driving.getPath().getEndPosition().equals(POINT.getPosition()))
                        .matches(driving -> driving.getPath().getEndPosition().equals(AFTER_MAP_MATCH.getPosition()));
                }
            }
        }

        @Nested
        @DisplayName("전송한 위치정보의 좌표계가 잘못된 경우")
        public final class Context_invalidCoordinateSystem {
            @Test
            @DisplayName("좌표계가 잘못되었음을 알려준다.")
            public void it_notifies_that_the_coordinate_system_is_invalid() {

            }
        }

        @Nested
        @DisplayName("전송한 위치정보의 좌표계가 잘못되지 않은 경우")
        public final class Context_validCoordinateSystem {
            @Test
            @DisplayName("오차를 보정 및 좌표계 변환을 수행한 위치정보를 저장한다.")
            public void it_saves_the_results_of_error_correction_and_coordinate_system_transformation() {
                subject(DrivingDataProto.DrivingData.newBuilder()
                    .setDrivingStatus(DrivingDataProto.DrivingStatus.DRIVING)
                    .setLatitude(-1D)
                    .setLongitude(-10D)
                    .setUserId(USER_ID)
                    .build());

            }
        }
    }
}
