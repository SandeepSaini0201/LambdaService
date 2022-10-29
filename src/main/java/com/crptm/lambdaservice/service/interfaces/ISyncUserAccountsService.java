package com.crptm.lambdaservice.service.interfaces;

import com.crptm.lambdaservice.controller.req.SyncUserAccountsFunctionReq;
import com.crptm.lambdaservice.enums.EnumLambdaStatus;

public interface ISyncUserAccountsService {
    EnumLambdaStatus syncUserAccounts(SyncUserAccountsFunctionReq request);
}
