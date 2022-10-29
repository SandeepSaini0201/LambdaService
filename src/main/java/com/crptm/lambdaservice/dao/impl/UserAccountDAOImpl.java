package com.crptm.lambdaservice.dao.impl;

import com.crptm.lambdaservice.client.aws.DynamoDBClient;
import com.crptm.lambdaservice.constants.DynamoDBConstants;
import com.crptm.lambdaservice.dao.converter.UserAccountEntityConverter;
import com.crptm.lambdaservice.dao.entity.UserAccountEntity;
import com.crptm.lambdaservice.dao.interfaces.IUserAccountDAO;
import com.crptm.lambdaservice.enums.EnumUserAccountStatus;
import com.crptm.lambdaservice.pojo.AccountNextStartingAfterKey;
import com.crptm.lambdaservice.pojo.UserAccount;
import com.crptm.lambdaservice.pojo.UserAccounts;
import com.crptm.lambdaservice.utils.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.UpdateItemEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.*;

@Repository
public class UserAccountDAOImpl implements IUserAccountDAO {

    @Autowired
    private DynamoDBClient client;

    @Autowired
    private UserAccountEntityConverter userAccountEntityConverter;

    @Override
    /* DAO method to fetch all direct integrated (DI) user accounts */
    public UserAccounts getUserAccountPageByLastEvaluatedUser(final AccountNextStartingAfterKey nextStartingAfterKey) {
        Map<String, AttributeValue> lastEvaluatedKey = null;
        if (nextStartingAfterKey != null) {
            lastEvaluatedKey = new HashMap<>();
            lastEvaluatedKey.put("userId", AttributeValue.builder()
                    .n(String.valueOf(nextStartingAfterKey.getUserId()))
                    .build());
            lastEvaluatedKey.put("accountNameAndUniqueId", AttributeValue.builder()
                    .s(nextStartingAfterKey.getAccountNameAndUniqueId())
                    .build());
        }

        Optional<Page<UserAccountEntity>> response = this.client.getUserAccountTable()
                .scan(this.getScanEnhancedRequestForDIUserAccount(lastEvaluatedKey))
                .stream()
                .filter(page -> !CollectionUtils.isEmpty(page.items()))
                .findFirst();

        if (response.isEmpty()) return null;
        Page<UserAccountEntity> page = response.get();
        List<UserAccount> userAccountList = new ArrayList<>();
        page.items()
                .forEach(ueEntity -> this.userAccountEntityConverter.convertToUserAccount(ueEntity)
                        .ifPresent(userAccountList::add));
        return this.getUserAccount(userAccountList, page.lastEvaluatedKey());
    }

    @Override
    public void updateUserAccountStatus(final UserAccount userAccount) {
        UpdateItemEnhancedRequest<UserAccountEntity> request = UpdateItemEnhancedRequest.builder(UserAccountEntity.class)
                .item(UserAccountEntity.builder()
                        .userId(userAccount.getUserId())
                        .accountNameAndUniqueId(userAccount.getAccountNameAndUniqueId())
                        .syncTxnStatus(userAccount.getSyncTxnStatus())
                        .version(userAccount.getVersion())
                        .build())
                .ignoreNulls(true)
                .build();
        this.client.getUserAccountTable().updateItem(request);
    }

    private ScanEnhancedRequest getScanEnhancedRequestForDIUserAccount(final Map<String, AttributeValue> lastEvaluatedKey) {
        return ScanEnhancedRequest.builder()
                .attributesToProject(CollectionUtil.getListFromString(DynamoDBConstants.REQUIRED_ATTRIBUTES))
                .exclusiveStartKey(lastEvaluatedKey)
                .limit(100)
                .filterExpression(this.getFilterExpressionForDIUserAccount())
                .build();
    }

    private Expression getFilterExpressionForDIUserAccount() {
        Map<String, AttributeValue> expressionValue = new HashMap<>();
        AttributeValue uaStatusAttributeValue = AttributeValue.builder()
                .s(EnumUserAccountStatus.CONNECTED.name())
                .build();
        expressionValue.put(":uaStatus", uaStatusAttributeValue);
        return Expression.builder()
                .expression("uaStatus = :uaStatus")
                .expressionValues(expressionValue)
                .build();
    }

    private UserAccounts getUserAccount(final List<UserAccount> userAccountList, final Map<String, AttributeValue> lastEvaluatedKey) {
        UserAccounts accountsResponse = UserAccounts.builder().userAccountList(userAccountList).build();

        if (lastEvaluatedKey != null && !lastEvaluatedKey.isEmpty()) {
            AccountNextStartingAfterKey accountNextStartingAfterKey = AccountNextStartingAfterKey.builder()
                    .userId(Long.parseLong(lastEvaluatedKey.get("userId").n()))
                    .accountNameAndUniqueId(lastEvaluatedKey.get("accountNameAndUniqueId").s())
                    .build();
            accountsResponse.setNextStartingAfter(accountNextStartingAfterKey);
        }
        return accountsResponse;
    }
}
