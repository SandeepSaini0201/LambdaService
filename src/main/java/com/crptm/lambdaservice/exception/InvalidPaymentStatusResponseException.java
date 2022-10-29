package com.crptm.lambdaservice.exception;

import com.crptm.lambdaservice.exception.base.BaseException;

public class InvalidPaymentStatusResponseException extends BaseException {

    public InvalidPaymentStatusResponseException(String message) {
        super(message);
    }
}
