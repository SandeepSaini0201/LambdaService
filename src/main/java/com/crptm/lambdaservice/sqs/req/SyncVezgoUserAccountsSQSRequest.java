package com.crptm.lambdaservice.sqs.req;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SyncVezgoUserAccountsSQSRequest {
    private Long userId;
    private String account;
    private String uniqueId;
    private String fiatCurrency;
}
