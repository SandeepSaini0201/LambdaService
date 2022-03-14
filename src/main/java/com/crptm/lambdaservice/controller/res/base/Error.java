package com.crptm.lambdaservice.controller.res.base;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
@Getter
public class Error {
    private Integer code;
    private String message;
}
