package com.crptm.lambdaservice.sqs.req;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SyncUETxnAndAccountSQSRequest {
    private Long userId;
    private String account;
    private String uniqueId;
    private Boolean buildUserPortfolio;
    private String fiatCurrency;
}
