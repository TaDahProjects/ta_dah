package com.tadah.vehicle.applications;

import com.tadah.vehicle.domains.entities.Vehicle;
import com.tadah.vehicle.domains.repositories.VehicleRepository;
import com.tadah.vehicle.domains.repositories.infra.JpaVehicleRepository;
import com.tadah.vehicle.dtos.DrivingDataProto;
import com.tadah.vehicle.exceptions.SendMessageFailException;
import com.tadah.vehicle.exceptions.VehicleNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.concurrent.BlockingQueue;

import static com.tadah.user.domains.entities.UserTest.USER_ID;
import static com.tadah.vehicle.domains.entities.VehicleTest.getLatitude;
import static com.tadah.vehicle.domains.entities.VehicleTest.getLongitude;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DataJpaTest
@DisplayName("VehicleService 클래스")
public class VehicleServiceTest {
    public static final DrivingDataProto.DrivingData START_DRIVING = DrivingDataProto.DrivingData.newBuilder()
        .setUserId(USER_ID)
        .setLatitude(getLatitude())
        .setLongitude(getLongitude())
        .setDrivingStatus(DrivingDataProto.DrivingStatus.START)
        .build();
    public static final DrivingDataProto.DrivingData UPDATE_DRIVING = DrivingDataProto.DrivingData.newBuilder()
        .setUserId(USER_ID)
        .setLatitude(getLatitude())
        .setLongitude(getLongitude())
        .setDrivingStatus(DrivingDataProto.DrivingStatus.DRIVING)
        .build();
    public static final DrivingDataProto.DrivingData STOP_DRIVING = DrivingDataProto.DrivingData.newBuilder()
        .setUserId(USER_ID)
        .setLatitude(getLatitude())
        .setLongitude(getLongitude())
        .setDrivingStatus(DrivingDataProto.DrivingStatus.STOP)
        .build();

    private final BlockingQueue<DrivingDataProto.DrivingData> blockingQueue;
    private final VehicleService vehicleService;
    private final VehicleRepository vehicleRepository;

    @Autowired
    private JpaVehicleRepository jpaVehicleRepository;

    public VehicleServiceTest(@Autowired final VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
        this.blockingQueue = mock(BlockingQueue.class);
        this.vehicleService = new VehicleService(vehicleRepository, blockingQueue);
    }

    private void mockSendData(final boolean isSuccess, final DrivingDataProto.DrivingData drivingData) {
        when(blockingQueue.offer(drivingData))
            .thenReturn(isSuccess);
    }

    private void verifySendData(final DrivingDataProto.DrivingData drivingData) {
        verify(blockingQueue, atMostOnce())
            .offer(drivingData);
    }

    @AfterEach
    private void afterEach() {
        jpaVehicleRepository.deleteAll();
    }


    @Nested
    @DisplayName("startDriving 메서드는")
    public final class Describe_startDriving {
        private void subject(final Double latitude, final Double longitude) {
            vehicleService.startDriving(USER_ID, latitude, longitude);
        }

        @BeforeEach
        private void beforeEach() {
            mockSendData(true, START_DRIVING);
        }

        @AfterEach
        private void afterEach() {
            verifySendData(START_DRIVING);
        }

        @Nested
        @DisplayName("메시지 전송에 실패한 경우")
        public final class Context_sendMessageFail {
            @BeforeEach
            private void beforeEach() {
                mockSendData(false, START_DRIVING);
            }

            @Test
            @DisplayName("SendMessageFailException을 던진다")
            public void it_throws_a_send_message_fail_exception() {
                assertThatThrownBy(() -> subject(getLatitude(), getLongitude()))
                    .isInstanceOf(SendMessageFailException.class);
            }
        }

        @Test
        @DisplayName("차량 운행을 시작 메시지를 전송한다")
        public void it_starts_the_driving() {
            subject(getLatitude(), getLongitude());
        }
    }

    @Nested
    @DisplayName("stopDriving 메서드는")
    public final class Describe_stopDriving {
        private void subject(final Double latitude, final Double longitude) {
            vehicleService.stopDriving(USER_ID, latitude, longitude);
        }

        @BeforeEach
        private void beforeEach() {
            mockSendData(true, STOP_DRIVING);
        }

        @AfterEach
        private void afterEach() {
            verifySendData(STOP_DRIVING);
        }

        @Nested
        @DisplayName("메시지 전송에 실패한 경우")
        public final class Context_sendMessageFail {
            @BeforeEach
            private void beforeEach() {
                mockSendData(false, STOP_DRIVING);
            }

            @Test
            @DisplayName("SendMessageFailException을 던진다")
            public void it_throws_a_send_message_fail_exception() {
                assertThatThrownBy(() -> subject(getLatitude(), getLongitude()))
                    .isInstanceOf(SendMessageFailException.class);
            }
        }

        @Test
        @DisplayName("차량 운행 종료 메시지를 전송한다")
        public void it_stops_the_driving() {
            subject(getLatitude(), getLongitude());
        }
    }

    @Nested
    @DisplayName("updateDriving 메서드는")
    public final class Describe_updateDriving {
        private void subject(final Double latitude, final Double longitude) {
            vehicleService.updateDriving(USER_ID, latitude, longitude);
        }

        @BeforeEach
        private void beforeEach() {
            mockSendData(true, UPDATE_DRIVING);
        }

        @AfterEach
        private void afterEach() {
            verifySendData(UPDATE_DRIVING);
        }

        @Nested
        @DisplayName("메시지 전송에 실패한 경우")
        public final class Context_sendMessageFail {
            @BeforeEach
            private void beforeEach() {
                mockSendData(false, UPDATE_DRIVING);
            }

            @Test
            @DisplayName("SendMessageFailException을 던진다")
            public void it_throws_a_send_message_fail_exception() {
                assertThatThrownBy(() -> subject(getLatitude(), getLongitude()))
                    .isInstanceOf(SendMessageFailException.class);
            }
        }

        @Test
        @DisplayName("차량 운행 정보를 업데이트한다")
        public void it_updates_the_driving_data() {
            subject(getLatitude(), getLongitude());
        }
    }
}
