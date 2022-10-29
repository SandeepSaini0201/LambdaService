package com.crptm.lambdaservice.enums;

import lombok.Getter;

@Getter
public enum EnumLambdaFunction {

    SYNC_USER_ACCOUNTS_FUNCTION("SyncUserAccountsFunction"),
    UPDATE_PAYMENT_STATUS("UpdatePaymentStatusFunction");

    private final String functionName;

    EnumLambdaFunction(String functionName) {
        this.functionName = functionName;
    }
}
