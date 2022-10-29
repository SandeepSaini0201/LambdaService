package com.crptm.lambdaservice.exception;

import com.crptm.lambdaservice.exception.base.BaseException;

public class SQSMessagingException extends BaseException {

    public SQSMessagingException(String message) {
        super(message);
    }

    public SQSMessagingException(String message, Throwable cause) {
        super(message, cause);
    }
}
