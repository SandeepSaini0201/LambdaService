package com.crptm.lambdaservice.validator.impl;

import com.crptm.lambdaservice.controller.req.base.BaseRequest;
import com.crptm.lambdaservice.validator.interfaces.IRequestValidator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractRequestValidator<T extends BaseRequest> implements IRequestValidator<T> {

}
