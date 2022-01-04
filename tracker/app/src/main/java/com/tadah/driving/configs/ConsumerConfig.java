package com.tadah.driving.configs;

import com.tadah.driving.kinesis.ConsumerProcessorFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

import java.util.UUID;

/**
 * Kinesis 레코트 수신 관련 설정을 수행한다.
 */
@Configuration
public class ConsumerConfig {
    private final String accessKey;
    private final String secretKey;
    private final Region awsRegion;
    private final String streamName;
    private final String applicationName;

    public ConsumerConfig(
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

    /**
     * Kinesis 레코드 수신 ConfigBuilder를 리턴한다.
     *
     * @return Kinesis 레코드 수신 ConfigBuilder
     */
    @Bean
    public ConfigsBuilder getConfigBuilder() {
        final KinesisAsyncClient kinesisClient = getKinesisClient();
        final ConsumerProcessorFactory consumerProcessorFactory = new ConsumerProcessorFactory();
        final DynamoDbAsyncClient dynamoClient = DynamoDbAsyncClient.builder().region(awsRegion).build();
        final CloudWatchAsyncClient cloudWatchClient = CloudWatchAsyncClient.builder().region(awsRegion).build();
        return new ConfigsBuilder(
            streamName, applicationName,
            kinesisClient, dynamoClient, cloudWatchClient,
            UUID.randomUUID().toString(), consumerProcessorFactory);
    }
}
