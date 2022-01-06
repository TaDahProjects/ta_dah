package com.tadah.driving.kinesis;

import software.amazon.kinesis.processor.ShardRecordProcessor;
import software.amazon.kinesis.processor.ShardRecordProcessorFactory;

/**
 * ConsumerProcessor를 생성한다.
 */
public final class ConsumerProcessorFactory implements ShardRecordProcessorFactory {
    @Override
    public ShardRecordProcessor shardRecordProcessor() {
        return new ConsumerProcessor();
    }
}
