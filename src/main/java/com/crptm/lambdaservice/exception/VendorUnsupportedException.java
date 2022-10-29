package com.crptm.lambdaservice.exception;

import com.crptm.lambdaservice.exception.base.BaseException;

public class VendorUnsupportedException extends BaseException {
    public VendorUnsupportedException(String message) {
        super(message);
    }
}
