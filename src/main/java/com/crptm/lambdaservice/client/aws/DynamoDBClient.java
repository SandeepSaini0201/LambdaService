package com.crptm.lambdaservice.client.aws;

import com.crptm.lambdaservice.dao.entity.UserAccountEntity;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import javax.annotation.PostConstruct;

@Component
public class DynamoDBClient {

    private DynamoDbEnhancedClient client;
    private DynamoDbTable<UserAccountEntity> userAccountTable;

    public DynamoDbTable<UserAccountEntity> getUserAccountTable() {
        return this.userAccountTable;
    }

    public DynamoDbEnhancedClient getClient() {
        return this.client;
    }

    @PostConstruct
    private void initialize() {
        this.client = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(getDynamoDBWebClient())
                .build();
        this.createTableResources();
    }

    private DynamoDbClient getDynamoDBWebClient() {
        return DynamoDbClient.builder()
                .region(Region.US_EAST_1)
                .build();
    }

    private void createTableResources() {
        this.userAccountTable = this.client.table("user_account", TableSchema.fromBean(UserAccountEntity.class));
    }
}
