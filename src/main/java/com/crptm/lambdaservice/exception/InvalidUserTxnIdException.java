package com.crptm.lambdaservice.exception;

import com.crptm.lambdaservice.exception.base.BaseException;

public class InvalidUserTxnIdException extends BaseException {

    private static final long serialVersionUID = -2894508592622545451L;

    public InvalidUserTxnIdException(String message) {
        super(message);
    }
}
