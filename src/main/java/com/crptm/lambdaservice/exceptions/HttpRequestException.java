package com.crptm.lambdaservice.exceptions;


import com.crptm.lambdaservice.exceptions.base.BaseException;

public class HttpRequestException extends BaseException {

    private static final long serialVersionUID = 7605369991762458595L;

    public HttpRequestException(String message) {
        super(message);
    }

    public HttpRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
