package com.crptm.lambdaservice.exception;

import com.crptm.lambdaservice.exception.base.BaseException;

public class TriggerSyncAccountException extends BaseException {

    public TriggerSyncAccountException(String message) {
        super(message);
    }

    public TriggerSyncAccountException(String message, Throwable cause) {
        super(message, cause);
    }
}
