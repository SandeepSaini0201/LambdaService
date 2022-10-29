package com.crptm.lambdaservice.accessors.interfaces;

import com.crptm.lambdaservice.accessors.res.PaymentStatusAccessorRes;
import com.crptm.lambdaservice.enums.EnumPaymentVendor;

public interface IPaymentStatusAccessor {
    PaymentStatusAccessorRes getPaymentStatusByVendorNameAndPaymentIntentId(EnumPaymentVendor paymentVendor, String vendorPaymentIntent);
}
