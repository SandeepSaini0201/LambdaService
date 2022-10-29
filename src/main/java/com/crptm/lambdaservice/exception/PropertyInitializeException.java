package com.crptm.lambdaservice.exception;

import com.crptm.lambdaservice.exception.base.BaseException;

public class PropertyInitializeException extends BaseException {

    public PropertyInitializeException(String message) {
        super(message);
    }

    public PropertyInitializeException(String message, Throwable cause) {
        super(message, cause);
    }
}
