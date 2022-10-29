package com.crptm.lambdaservice.client.aws;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaAsyncClientBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class LambdaClient {

    private AWSLambda lambdaClient;

    public AWSLambda getLambdaClient() {
        return this.lambdaClient;
    }

    @PostConstruct
    private void initializeClient() {
        this.lambdaClient = this.getAWSLambda();
    }

    private AWSLambda getAWSLambda() {
        return AWSLambdaAsyncClientBuilder.standard()
                .withRegion(Regions.US_EAST_1)
                .build();
    }
}
