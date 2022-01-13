package com.tadah;

import com.tadah.driving.utils.CoordinateUtil;
import org.opengis.referencing.FactoryException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Tracker {
    public static void main(String[] args) {
        SpringApplication.run(Tracker.class, args);
    }

    @Bean
    public CoordinateUtil getCoordinateUtil() throws FactoryException {
        return new CoordinateUtil();
    }
}
