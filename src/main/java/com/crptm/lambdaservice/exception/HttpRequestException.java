package com.crptm.lambdaservice.exception;

import com.crptm.lambdaservice.exception.base.BaseException;

public class HttpRequestException extends BaseException {

    private static final long serialVersionUID = -386341195234354900L;

    public HttpRequestException(String message) {
        super(message);
    }

    public HttpRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
