package com.crptm.lambdaservice;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.crptm.lambdaservice.controller.SyncUserAccountsController;
import com.crptm.lambdaservice.controller.req.SyncUserAccountsFunctionReq;
import com.crptm.lambdaservice.enums.EnumLambdaStatus;
import com.crptm.lambdaservice.utils.PropertiesUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Slf4j
public class SyncUserAccountsFunction implements RequestHandler<SyncUserAccountsFunctionReq, EnumLambdaStatus> {

    private static final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
            SyncUserAccountsFunction.class.getPackage().getName());
    @Autowired
    private SyncUserAccountsController controller;

    public SyncUserAccountsFunction() {
        context.getAutowireCapableBeanFactory().autowireBean(this);
    }

    @Override
    public EnumLambdaStatus handleRequest(SyncUserAccountsFunctionReq request, Context context) {
        PropertiesUtils.initializeProperties(request.getEnvironment());
        return this.controller.syncDirectIntegratedAccounts(request);
    }
}
