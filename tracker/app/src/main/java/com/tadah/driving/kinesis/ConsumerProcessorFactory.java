package com.tadah.driving.kinesis;

import org.springframework.stereotype.Component;
import software.amazon.kinesis.processor.ShardRecordProcessor;
import software.amazon.kinesis.processor.ShardRecordProcessorFactory;

/**
 * ConsumerProcessor를 생성한다.
 */
@Component
public final class ConsumerProcessorFactory implements ShardRecordProcessorFactory {
    @Override
    public ShardRecordProcessor shardRecordProcessor() {
        return new ConsumerProcessor();
    }
}
