package com.crptm.lambdaservice.controller.res;

import com.crptm.lambdaservice.enums.EnumEnvironment;
import com.crptm.lambdaservice.pojo.AccountNextStartingAfterKey;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SyncUserAccountsFunctionRes {
    private EnumEnvironment environment;
    private AccountNextStartingAfterKey nextStartingAfter;
}
