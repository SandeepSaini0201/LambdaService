package com.crptm.lambdaservice.controller;

import com.crptm.lambdaservice.enums.EnumLambdaStatus;
import com.crptm.lambdaservice.service.interfaces.IPaymentStatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PaymentStatusController {

    @Autowired
    private IPaymentStatusService paymentStatusService;

    public EnumLambdaStatus updatePaymentStatuses() {
        try {
            return paymentStatusService.updatePaymentStatus();
        } catch (Exception e) {
            log.error("Error occurred while updating payment statuses: {}", e.getMessage(), e);
            throw e;
        }
    }
}
