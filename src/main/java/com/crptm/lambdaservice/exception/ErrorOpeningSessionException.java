package com.crptm.lambdaservice.exception;

import com.crptm.lambdaservice.exception.base.BaseException;

public class ErrorOpeningSessionException extends BaseException {

    public ErrorOpeningSessionException(String message) {
        super(message);
    }

    public ErrorOpeningSessionException(String message, Throwable cause) {
        super(message, cause);
    }
}
