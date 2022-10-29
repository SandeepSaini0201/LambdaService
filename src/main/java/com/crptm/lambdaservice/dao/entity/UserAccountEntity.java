package com.crptm.lambdaservice.dao.entity;

import com.crptm.lambdaservice.constants.DBSecondaryIndexes;
import com.crptm.lambdaservice.enums.EnumSyncTxnStatus;
import com.crptm.lambdaservice.enums.EnumUserAccountStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.extensions.annotations.DynamoDbVersionAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@DynamoDbBean
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAccountEntity {

    private Long userId;
    private String accountNameAndUniqueId;
    private String uniqueAccountName;
    private String account;
    private String uniqueId;
    private String vendor;
    private EnumUserAccountStatus uaStatus;
    private EnumSyncTxnStatus syncTxnStatus;
    private Integer version;

    @DynamoDbPartitionKey
    @DynamoDbSecondaryPartitionKey(indexNames = {DBSecondaryIndexes.UNIQUE_ACCOUNT_NAME_LSI, DBSecondaryIndexes.ACCOUNT_LSI})
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @DynamoDbSortKey
    public String getAccountNameAndUniqueId() {
        return accountNameAndUniqueId;
    }

    public void setAccountNameAndUniqueId(String accountNameAndUniqueId) {
        this.accountNameAndUniqueId = accountNameAndUniqueId;
    }

    @DynamoDbAttribute(value = "uniqueAccountName")
    @DynamoDbSecondarySortKey(indexNames = {DBSecondaryIndexes.UNIQUE_ACCOUNT_NAME_LSI})
    public String getUniqueAccountName() {
        return uniqueAccountName;
    }

    public void setUniqueAccountName(String uniqueAccountName) {
        this.uniqueAccountName = uniqueAccountName;
    }

    @DynamoDbAttribute(value = "account")
    @DynamoDbSecondarySortKey(indexNames = {DBSecondaryIndexes.ACCOUNT_LSI})
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @DynamoDbAttribute(value = "uniqueId")
    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    @DynamoDbAttribute(value = "vendor")
    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    @DynamoDbAttribute(value = "uaStatus")
    public EnumUserAccountStatus getUaStatus() {
        return uaStatus;
    }

    public void setUaStatus(EnumUserAccountStatus uaStatus) {
        this.uaStatus = uaStatus;
    }

    @DynamoDbAttribute(value = "syncTxnStatus")
    public EnumSyncTxnStatus getSyncTxnStatus() {
        return syncTxnStatus;
    }

    public void setSyncTxnStatus(EnumSyncTxnStatus syncTxnStatus) {
        this.syncTxnStatus = syncTxnStatus;
    }

    @DynamoDbAttribute(value = "version")
    @DynamoDbVersionAttribute
    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
