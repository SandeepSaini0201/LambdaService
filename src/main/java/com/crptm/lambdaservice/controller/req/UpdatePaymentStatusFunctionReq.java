package com.crptm.lambdaservice.controller.req;

import com.crptm.lambdaservice.controller.req.base.BaseRequest;
import com.crptm.lambdaservice.enums.EnumEnvironment;
import lombok.Getter;

@Getter
public class UpdatePaymentStatusFunctionReq extends BaseRequest {
    private EnumEnvironment environment;
}
