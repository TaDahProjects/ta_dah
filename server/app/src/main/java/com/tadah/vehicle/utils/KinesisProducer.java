package com.tadah.vehicle.utils;

import com.tadah.vehicle.dtos.DrivingDataProto;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Supplier;

/**
 * AWS Kinesis Stream 전송을 수행한다
 */
@Component
public final class KinesisProducer {
    private final BlockingQueue<DrivingDataProto.DrivingData> blockingQueue;

    public KinesisProducer() {
        this.blockingQueue = new LinkedBlockingQueue<>();
    }

    /**
     * Kinesis Producer 등록한다
     *
     * @return Kinesis Producer
     */
    @Bean
    public Supplier<DrivingDataProto.DrivingData> produceDriving() {
        return this.blockingQueue::poll;
    }

    public boolean sendData(final DrivingDataProto.DrivingData drivingData) {
        return this.blockingQueue.offer(drivingData);
    }
}
