package com.crptm.lambdaservice.pojo;

import com.crptm.lambdaservice.enums.EnumCurrency;
import com.crptm.lambdaservice.enums.EnumRefundStatus;
import com.crptm.lambdaservice.enums.EnumTxnType;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefundTxnPOJO {
    private Long id;
    private String merchantId;
    private String userId;
    private String paymentIntentId;
    private String vendorPaymentIntentId;
    private Double amount;
    private EnumCurrency currency;
    private EnumRefundStatus status;
    private EnumTxnType txnType;
    private String metadata;
    private Instant txnDate;
    private Instant createdAt;
    private Instant updatedAt;
    private UserTxnPOJO userTxnPOJO;
    private Long version;
}
