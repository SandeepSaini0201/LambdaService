package com.crptm.lambdaservice.dao.converter;

import com.crptm.lambdaservice.dao.entity.UserAccountEntity;
import com.crptm.lambdaservice.pojo.UserAccount;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserAccountEntityConverter {

    public Optional<UserAccount> convertToUserAccount(final UserAccountEntity userAccountEntity) {
        if (userAccountEntity == null) {
            return Optional.empty();
        }
        return Optional.of(UserAccount.builder()
                .userId(userAccountEntity.getUserId())
                .accountNameAndUniqueId(userAccountEntity.getAccountNameAndUniqueId())
                .uniqueAccountName(userAccountEntity.getUniqueAccountName())
                .account(userAccountEntity.getAccount())
                .uniqueId(userAccountEntity.getUniqueId())
                .vendor(userAccountEntity.getVendor())
                .uaStatus(userAccountEntity.getUaStatus())
                .syncTxnStatus(userAccountEntity.getSyncTxnStatus())
                .version(userAccountEntity.getVersion())
                .build());
    }
}
