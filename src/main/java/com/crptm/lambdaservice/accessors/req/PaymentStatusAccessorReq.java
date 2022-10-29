package com.crptm.lambdaservice.accessors.req;

import com.crptm.lambdaservice.enums.EnumPaymentVendor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentStatusAccessorReq {
    private EnumPaymentVendor paymentVendor;
    private String vendorPaymentIntentId;
}
