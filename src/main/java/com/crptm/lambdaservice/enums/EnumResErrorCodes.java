package com.crptm.lambdaservice.enums;

import lombok.Getter;

@Getter
public enum EnumResErrorCodes {

    HTTP_REQ_FAILURE(9982, "Exception occurred during making HTTP request"),
    HTTP_RES_FAILURE(9983, "HTTP request status is not successful."),
    HTTP_RES_PARSE_FAILURE(9984, "Error in parsing HTTP response."),
    INTERNAL_ERROR(9999, "Something went wrong and our team is working on it.");

    private final Integer code;
    private final String message;

    EnumResErrorCodes(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static EnumResErrorCodes getByResCode(Integer code) {
        for (final EnumResErrorCodes enumResErrorCodes : EnumResErrorCodes.values()) {
            if (enumResErrorCodes.getCode().equals(code)) {
                return enumResErrorCodes;
            }
        }
        return null;
    }

}
