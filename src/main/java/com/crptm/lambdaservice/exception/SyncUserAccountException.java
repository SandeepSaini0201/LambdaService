package com.crptm.lambdaservice.exception;

import com.crptm.lambdaservice.exception.base.BaseException;

public class SyncUserAccountException extends BaseException {

    public SyncUserAccountException(String message) {
        super(message);
    }

    public SyncUserAccountException(String message, Throwable cause) {
        super(message, cause);
    }
}
