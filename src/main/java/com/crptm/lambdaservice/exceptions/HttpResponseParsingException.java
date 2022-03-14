package com.crptm.lambdaservice.exceptions;

import com.crptm.lambdaservice.exceptions.base.BaseException;

public class HttpResponseParsingException extends BaseException {

    private static final long serialVersionUID = 7605369990823458595L;

    public HttpResponseParsingException(String message) {
        super(message);
    }

    public HttpResponseParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
