package com.crptm.lambdaservice;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.crptm.lambdaservice.client.MySQLClient;
import com.crptm.lambdaservice.controller.PaymentStatusController;
import com.crptm.lambdaservice.controller.req.UpdatePaymentStatusFunctionReq;
import com.crptm.lambdaservice.enums.EnumLambdaStatus;
import com.crptm.lambdaservice.utils.PropertiesUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Slf4j
public class UpdatePaymentStatusFunction implements RequestHandler<UpdatePaymentStatusFunctionReq, EnumLambdaStatus> {

    private static final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
            UpdatePaymentStatusFunction.class.getPackage().getName());

    @Autowired
    private PaymentStatusController paymentStatusController;

    @Autowired
    private MySQLClient mySQLClient;

    public UpdatePaymentStatusFunction() {
        context.getAutowireCapableBeanFactory().autowireBean(this);
    }

    @Override
    public EnumLambdaStatus handleRequest(UpdatePaymentStatusFunctionReq updatePaymentStatusFunctionReq, Context context) {
        try {
            PropertiesUtils.initializeProperties(updatePaymentStatusFunctionReq.getEnvironment());
            return this.paymentStatusController.updatePaymentStatuses();
        } finally {
            mySQLClient.getSessionFactory().close();
        }
    }
}
