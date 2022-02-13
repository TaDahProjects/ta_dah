package com.tadah.driving.configs;

import com.tadah.driving.applications.DrivingService;
import com.tadah.driving.domains.entities.Driving;
import com.tadah.driving.dtos.DrivingDataProto;
import org.geolatte.geom.C2D;
import org.geolatte.geom.Point;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Kinesis Stream Consumer를 등록한다
 */
@Configuration
public class KinesisConsumerConfiguration {
    private final DrivingService drivingService;

    public KinesisConsumerConfiguration(final DrivingService drivingService) {
        this.drivingService = drivingService;
    }

    @Bean
    public Consumer<DrivingDataProto.DrivingData> processDriving() {
        return drivingData -> {
            final Optional<Driving> drivingOptional = drivingService.get(drivingData.getUserId());
            if (drivingOptional.isEmpty() && !drivingData.getDrivingStatus().equals(DrivingDataProto.DrivingStatus.START)) {
                // todo radis pub/sub 연동 후 추가 필요
            }
            if (drivingOptional.isPresent() && drivingData.getDrivingStatus().equals(DrivingDataProto.DrivingStatus.START)) {
                // todo radis pub/sub 연동 후 추가 필요
            }

            try {
                final Point<C2D> point = drivingService.transForm(drivingData.getLatitude(), drivingData.getLongitude());
                // 분기
                final Driving driving = drivingOptional.orElse(new Driving(drivingData.getUserId(), point));

                switch (drivingData.getDrivingStatus()) {
                    case START -> drivingService.start(driving);
                    case DRIVING -> drivingService.update(driving, point);
                    case STOP -> drivingService.stop(driving, point);
                }
            } catch (Exception exception) {
                // todo radis pub/sub 연동 후 추가 필요
            }
        };
    }
}
