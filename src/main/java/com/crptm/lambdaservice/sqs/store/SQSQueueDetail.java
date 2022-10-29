package com.crptm.lambdaservice.sqs.store;

import com.amazonaws.AmazonClientException;
import com.crptm.lambdaservice.client.aws.SQSClient;
import com.crptm.lambdaservice.enums.EnumSQSQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class SQSQueueDetail {

    private final Map<EnumSQSQueue, String> map = new HashMap<>();
    @Autowired
    private SQSClient sqsClient;

    public String getURLByQueueName(final EnumSQSQueue queue) {
        return this.map.get(queue);
    }

    @PostConstruct
    private void fetchingSQSQueueDetails() {
        EnumSQSQueue.getAllSQSQueue().forEach(queue -> {
            try {
                this.map.put(queue, this.sqsClient.getSqsClient().getQueueUrl(queue.getQueueName()).getQueueUrl());
            } catch (AmazonClientException e) {
                log.error("Failure in connecting with client , due to : {}", e.getMessage());
            }
        });
    }
}
