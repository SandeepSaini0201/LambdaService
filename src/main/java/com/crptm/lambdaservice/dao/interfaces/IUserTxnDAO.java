package com.crptm.lambdaservice.dao.interfaces;

import com.crptm.lambdaservice.enums.EnumPaymentStatus;
import com.crptm.lambdaservice.pojo.UserTxnPOJO;

import java.time.Instant;
import java.util.List;

public interface IUserTxnDAO {

    List<UserTxnPOJO> findTxnsByPaymentStatusAndBeforeInstant(EnumPaymentStatus paymentStatusList, Instant beforeDate);

    void updatePaymentStatusByTxnId(Long txnId, EnumPaymentStatus paymentStatus);

    void updatePaymentStatusByStatusAndBeforeInstant(EnumPaymentStatus newPaymentStatus, EnumPaymentStatus oldPaymentStatus, Instant beforeInstant);
}
