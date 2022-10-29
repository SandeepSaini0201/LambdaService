package com.crptm.lambdaservice.pojo;

import com.crptm.lambdaservice.enums.*;
import lombok.*;

import java.time.Instant;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserTxnPOJO {
    private Long id;
    private String merchantId;
    private String userId;
    private String paymentIntentId;
    private String vendorPaymentIntentId;
    private Double amount;
    private EnumCurrency currency;
    private EnumPaymentStatus status;
    private EnumTxnType txnType;
    private EnumPaymentVendor paymentVendor;
    private EnumPaymentMethod paymentMethod;
    private EnumPaymentMethodFutureUsage futureUsage;
    private String returnURL;
    private String metadata;
    private Instant txnDate;
    private Instant createdAt;
    private Instant updatedAt;
    private Long version;
}
