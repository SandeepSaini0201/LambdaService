package com.crptm.lambdaservice.enums;

import com.crptm.lambdaservice.constants.ErrorConstants;
import com.crptm.lambdaservice.exception.EmptyDataException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Slf4j
public enum EnumPaymentMethod {
    CARD;

    private static final Map<String, EnumPaymentMethod> map;

    static {
        map = Arrays.stream(EnumPaymentMethod.values()).collect(Collectors.toMap(EnumPaymentMethod::name, e -> e));
    }

    public static EnumPaymentMethod getEnumPaymentMethodByMethodName(final String methodName) {
        EnumPaymentMethod enumPaymentMethod = map.get(methodName);
        if (enumPaymentMethod == null) {
            String errorMsg = String.format(ErrorConstants.PAYMENT_METHOD_NOT_FOUND, methodName);
            log.error(errorMsg);
            throw new EmptyDataException(errorMsg);
        }
        return enumPaymentMethod;
    }
}
