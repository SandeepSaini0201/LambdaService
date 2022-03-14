package com.crptm.lambdaservice.exceptions;


import com.crptm.lambdaservice.exceptions.base.BaseException;

public class HttpResponseException extends BaseException {

    private static final long serialVersionUID = 7605366691762458595L;

    public HttpResponseException(String message) {
        super(message);
    }

    public HttpResponseException(String message, Throwable cause) {
        super(message, cause);
    }
}
