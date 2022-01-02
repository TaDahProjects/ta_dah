package com.tadah;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
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
