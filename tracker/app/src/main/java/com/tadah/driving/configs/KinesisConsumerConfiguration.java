package com.tadah.driving.configs;

import com.tadah.driving.applications.DrivingService;
import com.tadah.driving.dtos.DrivingDataProto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
        return System.out::println;
    }
}
