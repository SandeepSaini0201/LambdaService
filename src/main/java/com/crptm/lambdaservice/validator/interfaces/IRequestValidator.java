package com.crptm.lambdaservice.validator.interfaces;


import com.crptm.lambdaservice.controller.req.base.BaseRequest;

public interface IRequestValidator<T extends BaseRequest> {
    void validate(T request);
}
