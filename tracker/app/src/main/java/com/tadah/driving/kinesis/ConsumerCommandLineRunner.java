package com.tadah.driving.kinesis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatch.CloudWatchAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient;
import software.amazon.awssdk.services.kinesis.KinesisAsyncClientBuilder;
import software.amazon.kinesis.common.ConfigsBuilder;
import software.amazon.kinesis.common.KinesisClientUtil;
import software.amazon.kinesis.coordinator.Scheduler;

import java.util.UUID;

/**
 * Kinesis 레코트 수신 관련 설정을 수행한다.
 */
@Component
public class ConsumerCommandLineRunner implements CommandLineRunner {
    private final String accessKey;
    private final String secretKey;
    private final Region awsRegion;
    private final String streamName;
    private final String applicationName;

    public ConsumerCommandLineRunner(
        @Value("${spring.aws.access_key}") final String accessKey,
        @Value("${spring.aws.secret_key}") final String secretKey, @Value("${spring.aws.region}") final String awsRegion,
        @Value("${spring.aws.stream_name}") final String streamName, @Value("${spring.aws.application_name}") final String applicationName
    ) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.awsRegion = Region.of(awsRegion);
        this.streamName = streamName;
        this.applicationName = applicationName;
    }

    private KinesisAsyncClient getKinesisClient() {
        final AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(accessKey, secretKey);
        final AwsCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(awsBasicCredentials);
        final KinesisAsyncClientBuilder kinesisAsyncClientBuilder = KinesisAsyncClient.builder()
            .credentialsProvider(credentialsProvider)
            .region(awsRegion);
        return KinesisClientUtil.createKinesisAsyncClient(kinesisAsyncClientBuilder);
    }

    private ConfigsBuilder getConfigBuilder() {
        final KinesisAsyncClient kinesisClient = getKinesisClient();
        final ConsumerProcessorFactory consumerProcessorFactory = new ConsumerProcessorFactory();
        final DynamoDbAsyncClient dynamoClient = DynamoDbAsyncClient.builder().region(awsRegion).build();
        final CloudWatchAsyncClient cloudWatchClient = CloudWatchAsyncClient.builder().region(awsRegion).build();
        return new ConfigsBuilder(
            streamName, applicationName,
            kinesisClient, dynamoClient, cloudWatchClient,
            UUID.randomUUID().toString(), consumerProcessorFactory);
    }

    /**
     * Kinesis 레코드를 수신한다.
     */
    @Override
    public void run(String... args) {
        final ConfigsBuilder configsBuilder = getConfigBuilder();

        final Scheduler scheduler = new Scheduler(
            configsBuilder.checkpointConfig(), configsBuilder.coordinatorConfig(),
            configsBuilder.leaseManagementConfig(), configsBuilder.lifecycleConfig(),
            configsBuilder.metricsConfig(), configsBuilder.processorConfig(), configsBuilder.retrievalConfig());

        final Thread schedulerThread = new Thread(scheduler);
        schedulerThread.setDaemon(true);
        schedulerThread.start();
    }
}
