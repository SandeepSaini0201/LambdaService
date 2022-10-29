package com.crptm.lambdaservice.exception;

import com.crptm.lambdaservice.exception.base.BaseException;

public class EmptyDataException extends BaseException {
    public EmptyDataException(String message) {
        super(message);
    }
}
