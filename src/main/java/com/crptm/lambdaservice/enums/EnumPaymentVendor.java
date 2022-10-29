package com.crptm.lambdaservice.enums;

import com.crptm.lambdaservice.exception.VendorUnsupportedException;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public enum EnumPaymentVendor {
    STRIPE;

    private static final Map<String, EnumPaymentVendor> map;

    static {
        map = Arrays.stream(EnumPaymentVendor.values()).collect(Collectors.toMap(EnumPaymentVendor::name, e -> e));
    }

    public static EnumPaymentVendor getEnumPaymentVendorByVendorName(final String vendorName) {
        EnumPaymentVendor enumPaymentVendor = map.get(vendorName);
        if (enumPaymentVendor == null) {
            String message = String.format("Vendor is unsupported: %s", vendorName);
            throw new VendorUnsupportedException(message);
        }
        return enumPaymentVendor;
    }
}
