package com.tadah;

import com.bedatadriven.jackson.datatype.jts.JtsModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import software.amazon.kinesis.common.ConfigsBuilder;
import software.amazon.kinesis.coordinator.Scheduler;

@SpringBootApplication
public class Tracker implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(Tracker.class, args);
    }

    private final ApplicationContext applicationContext;
    public Tracker(@Autowired final ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
   }

    /**
     * Kinesis 레코드를 수신한다.
     */
    @Override
    public void run(String... args) {
        final ConfigsBuilder configsBuilder = applicationContext.getBean(ConfigsBuilder.class);

        final Scheduler scheduler = new Scheduler(
            configsBuilder.checkpointConfig(), configsBuilder.coordinatorConfig(),
            configsBuilder.leaseManagementConfig(), configsBuilder.lifecycleConfig(),
            configsBuilder.metricsConfig(), configsBuilder.processorConfig(), configsBuilder.retrievalConfig());

        final Thread schedulerThread = new Thread(scheduler);
        schedulerThread.setDaemon(true);
        schedulerThread.start();
    }
}
