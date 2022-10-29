package com.crptm.lambdaservice.controller.req;

import com.crptm.lambdaservice.controller.req.base.BaseRequest;
import com.crptm.lambdaservice.enums.EnumEnvironment;
import com.crptm.lambdaservice.pojo.AccountNextStartingAfterKey;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SyncUserAccountsFunctionReq extends BaseRequest {
    private EnumEnvironment environment;
    private AccountNextStartingAfterKey nextStartingAfter;
}
