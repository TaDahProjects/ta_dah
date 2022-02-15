package com.tadah;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.converter.ProtobufMessageConverter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class TaDah {
    public static void main(String[] args) {
        SpringApplication.run(TaDah.class, args);
    }

    @Bean
    public static PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public static MessageConverter getMessageConverter() {
        return new ProtobufMessageConverter();
    }
}
