package com.crptm.lambdaservice.service.impl;

import com.crptm.lambdaservice.accessors.interfaces.IPaymentStatusAccessor;
import com.crptm.lambdaservice.accessors.res.PaymentStatusAccessorRes;
import com.crptm.lambdaservice.dao.impl.UserTxnDAO;
import com.crptm.lambdaservice.dao.interfaces.IUserTxnDAO;
import com.crptm.lambdaservice.enums.EnumLambdaStatus;
import com.crptm.lambdaservice.enums.EnumPaymentStatus;
import com.crptm.lambdaservice.exception.InvalidPaymentStatusResponseException;
import com.crptm.lambdaservice.pojo.UserTxnPOJO;
import com.crptm.lambdaservice.service.interfaces.IPaymentStatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@Slf4j
public class PaymentStatusService implements IPaymentStatusService {

    private static final Long PAYMENTS_MINIMUM_AGE_IN_SECONDS = 1200L;

    @Autowired
    private IUserTxnDAO userTxnDAO;

    @Autowired
    private IPaymentStatusAccessor paymentStatusAccessor;

    public EnumLambdaStatus updatePaymentStatus() {

        /*Threshold age of Txn.*/
        Instant beforeInstant = Instant.now().minusSeconds(PAYMENTS_MINIMUM_AGE_IN_SECONDS);

        /*Update UserTxns IN_PROGRESS to CANCELLED , before instant*/
        this.userTxnDAO.updatePaymentStatusByStatusAndBeforeInstant(EnumPaymentStatus.CANCELLED, EnumPaymentStatus.IN_PROGRESS, beforeInstant);

        /*Find UserTxns SENT_TO_VENDOR, before instant*/
        List<UserTxnPOJO> userTxnsSentToVendor = this.userTxnDAO.findTxnsByPaymentStatusAndBeforeInstant(EnumPaymentStatus.SENT_TO_VENDOR, beforeInstant);

        /*Update txns SENT_TO_VENDOR with accessor response*/
        for (UserTxnPOJO userTxnPOJO : userTxnsSentToVendor) {
            PaymentStatusAccessorRes paymentStatusAccessorRes = this.paymentStatusAccessor
                    .getPaymentStatusByVendorNameAndPaymentIntentId(userTxnPOJO.getPaymentVendor(), userTxnPOJO.getVendorPaymentIntentId());

            /*If either amount or currency doesn't match with accessor response, this throws exception*/
            if ((paymentStatusAccessorRes.getAmount() != userTxnPOJO.getAmount()) || (paymentStatusAccessorRes.getCurrency() != userTxnPOJO.getCurrency())) {
                String message = String.format("Error matching Payments details, UserTxn Id: %s", userTxnPOJO.getId());
                log.error(message);
                throw new InvalidPaymentStatusResponseException(message + userTxnPOJO.getId());
            }
            this.userTxnDAO.updatePaymentStatusByTxnId(userTxnPOJO.getId(), paymentStatusAccessorRes.getPaymentStatus());
        }
        return EnumLambdaStatus.SUCCESS;
    }
}
