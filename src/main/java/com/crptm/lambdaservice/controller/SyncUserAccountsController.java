package com.crptm.lambdaservice.controller;

import com.crptm.lambdaservice.controller.req.SyncUserAccountsFunctionReq;
import com.crptm.lambdaservice.enums.EnumLambdaStatus;
import com.crptm.lambdaservice.pojo.AccountNextStartingAfterKey;
import com.crptm.lambdaservice.service.interfaces.ISyncUserAccountsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SyncUserAccountsController {

    @Autowired
    private ISyncUserAccountsService service;

    public EnumLambdaStatus syncDirectIntegratedAccounts(SyncUserAccountsFunctionReq request) {
        try {
            return this.service.syncUserAccounts(request);
        } catch (Exception e) {
            if (request.getNextStartingAfter() == null) {
                log.error("Error occurred due to : {}, need to start from starting.", e.getMessage(), e);
            } else {
                AccountNextStartingAfterKey nextStartingAfter = request.getNextStartingAfter();
                log.error("Error occurred due to : {}, need to start from user : {} and account : {}", e.getMessage(), nextStartingAfter.getUserId(), nextStartingAfter.getAccountNameAndUniqueId());
            }
            throw e;
        }
    }
}
