package com.crptm.lambdaservice.accessors.impl;

import com.crptm.lambdaservice.accessors.interfaces.IPaymentStatusAccessor;
import com.crptm.lambdaservice.accessors.req.PaymentStatusAccessorReq;
import com.crptm.lambdaservice.accessors.res.PaymentStatusAccessorRes;
import com.crptm.lambdaservice.enums.EnumPaymentVendor;
import com.crptm.lambdaservice.exception.PaymentStatusFetchException;
import com.crptm.lambdaservice.http.HttpSyncClient;
import com.crptm.lambdaservice.utils.PropertiesUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.Collections;

@Slf4j
@Repository
public class PaymentStatusAccessor implements IPaymentStatusAccessor {

    @Autowired
    private HttpSyncClient httpSyncClient;

    private String paymentGatewayBaseUrl = PropertiesUtils.getProps().getProperty("pgs.base.url");

    private static final String PAYMENT_STATUS_FROM_GATEWAY_ENDPOINT ="/gateway/status";

    @Override
    public PaymentStatusAccessorRes getPaymentStatusByVendorNameAndPaymentIntentId(EnumPaymentVendor paymentVendor, String vendorPaymentIntent) {
        PaymentStatusAccessorRes response = httpSyncClient.post(PaymentStatusAccessorRes.class,
                this.paymentGatewayBaseUrl.concat(PAYMENT_STATUS_FROM_GATEWAY_ENDPOINT),
                Collections.emptyMap(),
                Collections.emptyMap(),
                this.getRequestBody(paymentVendor, vendorPaymentIntent));

        if (response.getCode() == HttpStatus.OK.value()) return response;
        String message = "Error occurred while fetching payment status from Payment Gateway Service.";
        log.error(message);
        throw new PaymentStatusFetchException(message);
    }

    private PaymentStatusAccessorReq getRequestBody(EnumPaymentVendor paymentVendor, String vendorPaymentIntent) {
        return PaymentStatusAccessorReq.builder()
                .paymentVendor(paymentVendor)
                .vendorPaymentIntentId(vendorPaymentIntent)
                .build();
    }
}
