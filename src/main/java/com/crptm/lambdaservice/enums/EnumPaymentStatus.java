package com.crptm.lambdaservice.enums;

import lombok.Getter;

@Getter
public enum EnumPaymentStatus {
    IN_PROGRESS, SUCCESS, SENT_TO_VENDOR, CANCELLED, FAILED, ERROR
}
