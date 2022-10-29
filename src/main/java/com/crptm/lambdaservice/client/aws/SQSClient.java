package com.crptm.lambdaservice.client.aws;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
@Getter
public class SQSClient {

    private AmazonSQSAsync sqsClient;

    @Value("${aws.region}")
    private String region;

    @Bean
    public QueueMessagingTemplate queueMessagingTemplate() {
        return new QueueMessagingTemplate(this.sqsClient);
    }

    @PostConstruct
    private void initialize() {
        this.sqsClient = getSQSWebClient();
        log.info("Initializing SQS Client using Container Credentials");
    }

    private AmazonSQSAsync getSQSWebClient() {
        return AmazonSQSAsyncClientBuilder
                .standard()
                .withRegion(region)
                .build();
    }
}
