package com.tadah;

import com.tadah.driving.utils.CoordinateUtil;
import com.tadah.driving.utils.ProtobufMessageConverter;
import org.opengis.referencing.FactoryException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.converter.MessageConverter;

@SpringBootApplication
public class Tracker {
    public static void main(String[] args) {
        SpringApplication.run(Tracker.class, args);
    }

    @Bean
    public CoordinateUtil getCoordinateUtil() throws FactoryException {
        return new CoordinateUtil();
    }
    @Bean
    public static MessageConverter getMessageConverter() {
        return new ProtobufMessageConverter();
    }
}
