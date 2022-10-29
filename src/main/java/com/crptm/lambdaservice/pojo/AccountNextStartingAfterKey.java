package com.crptm.lambdaservice.pojo;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AccountNextStartingAfterKey {
    private Long userId;
    private String accountNameAndUniqueId;
}
