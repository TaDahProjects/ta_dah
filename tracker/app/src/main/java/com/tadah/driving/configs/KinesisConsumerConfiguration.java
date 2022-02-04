package com.tadah.driving.configs;

import com.tadah.driving.applications.DrivingService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class KinesisConsumerConfiguration {
    private final DrivingService drivingService;

    public KinesisConsumerConfiguration(final DrivingService drivingService) {
        this.drivingService = drivingService;
    }

    @Bean
    public Consumer<String> processDriving() {
        return System.out::println;
    }
}
