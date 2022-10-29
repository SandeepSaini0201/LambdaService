package com.crptm.lambdaservice.exception;

import com.crptm.lambdaservice.exception.base.BaseException;

public class PaymentStatusFetchException extends BaseException {

    private static final long serialVersionUID = -4542063062241894880L;

    public PaymentStatusFetchException(String message) {
        super(message);
    }
}
