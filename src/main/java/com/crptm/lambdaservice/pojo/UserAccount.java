package com.crptm.lambdaservice.pojo;

import com.crptm.lambdaservice.enums.EnumSyncTxnStatus;
import com.crptm.lambdaservice.enums.EnumUserAccountStatus;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserAccount {
    private Long userId;
    private String accountNameAndUniqueId;
    private String uniqueAccountName;
    private String account;
    private String uniqueId;
    private String vendor;
    private EnumUserAccountStatus uaStatus;
    private EnumSyncTxnStatus syncTxnStatus;
    private Integer version;
}