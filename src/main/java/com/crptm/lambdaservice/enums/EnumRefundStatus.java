package com.crptm.lambdaservice.enums;

import lombok.Getter;

@Getter
public enum EnumRefundStatus {
    IN_PROGRESS, SUCCESS, SENT_TO_VENDOR, CANCELLED, FAILED, ERROR
}
