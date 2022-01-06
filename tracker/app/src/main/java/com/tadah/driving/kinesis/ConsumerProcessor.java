package com.tadah.driving.kinesis;

import software.amazon.kinesis.lifecycle.events.InitializationInput;
import software.amazon.kinesis.lifecycle.events.LeaseLostInput;
import software.amazon.kinesis.lifecycle.events.ProcessRecordsInput;
import software.amazon.kinesis.lifecycle.events.ShardEndedInput;
import software.amazon.kinesis.lifecycle.events.ShutdownRequestedInput;
import software.amazon.kinesis.processor.ShardRecordProcessor;
import software.amazon.kinesis.retrieval.KinesisClientRecord;

/**
 * Kinesis 레코드를 수신한다.
 */
public final class ConsumerProcessor implements ShardRecordProcessor {
    @Override
    public void initialize(final InitializationInput initializationInput) {
    }

    private void printRecord(final KinesisClientRecord record) {
        final byte[] bytes = new byte[record.data().remaining()];
        record.data().get(bytes);
        System.out.println(new String(bytes));
    }

    @Override
    public void processRecords(final ProcessRecordsInput processRecordsInput) {
        processRecordsInput.records().forEach(this::printRecord);
    }

    @Override
    public void leaseLost(final LeaseLostInput leaseLostInput) {
    }

    @Override
    public void shardEnded(final ShardEndedInput shardEndedInput) {
    }

    @Override
    public void shutdownRequested(final ShutdownRequestedInput shutdownRequestedInput) {
    }
}
