package com.crptm.lambdaservice.pojo;

import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserAccounts {
    private List<UserAccount> userAccountList;
    private AccountNextStartingAfterKey nextStartingAfter;
}
