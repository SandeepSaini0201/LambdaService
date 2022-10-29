package com.crptm.lambdaservice.enums;

import lombok.Getter;

import java.util.List;

@Getter
public enum EnumSQSQueue {

    SYNC_TRANSACTION_QUEUE("SyncTransactionsQueue"),
    SYNC_VEZGO_USER_ACCOUNTS_QUEUE("SyncVezgoUserAccountsQueue");

    private final String queueName;

    EnumSQSQueue(String queueName) {
        this.queueName = queueName;
    }

    public static List<EnumSQSQueue> getAllSQSQueue() {
        return List.of(EnumSQSQueue.values());
    }
}
