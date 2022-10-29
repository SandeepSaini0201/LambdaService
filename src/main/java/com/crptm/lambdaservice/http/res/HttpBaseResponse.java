package com.crptm.lambdaservice.http.res;

import lombok.Data;

@Data
public class HttpBaseResponse {
    private Integer code;
    private Error error;
}
