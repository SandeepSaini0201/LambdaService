package com.crptm.lambdaservice.enums;

import lombok.Getter;

/**
 * ON_SESSION -> user payment method used to make payment will be not saved
 * OFF_SESSION -> user payment method used to make payment will be saved
 */

@Getter
public enum EnumPaymentMethodFutureUsage {
    EMPTY,
    ON_SESSION,
    OFF_SESSION
}
