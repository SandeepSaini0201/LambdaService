package com.crptm.lambdaservice.service.impl;

import com.amazonaws.services.lambda.model.InvokeRequest;
import com.crptm.lambdaservice.client.aws.LambdaClient;
import com.crptm.lambdaservice.controller.req.SyncUserAccountsFunctionReq;
import com.crptm.lambdaservice.dao.interfaces.IUserAccountDAO;
import com.crptm.lambdaservice.enums.EnumLambdaFunction;
import com.crptm.lambdaservice.enums.EnumLambdaStatus;
import com.crptm.lambdaservice.enums.EnumSQSQueue;
import com.crptm.lambdaservice.enums.EnumSyncTxnStatus;
import com.crptm.lambdaservice.pojo.AccountNextStartingAfterKey;
import com.crptm.lambdaservice.pojo.UserAccount;
import com.crptm.lambdaservice.pojo.UserAccounts;
import com.crptm.lambdaservice.service.interfaces.ISyncUserAccountsService;
import com.crptm.lambdaservice.sqs.producer.SQSProducer;
import com.crptm.lambdaservice.sqs.req.SyncUETxnAndAccountSQSRequest;
import com.crptm.lambdaservice.sqs.req.SyncVezgoUserAccountsSQSRequest;
import com.crptm.lambdaservice.utils.MapperUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class SyncUserAccountsServiceImpl implements ISyncUserAccountsService {

    private static final Boolean BUILD_PORTFOLIO = Boolean.TRUE;
    /*As of now we hard code the By-Default currency to build user portfolio*/
    private static final String FIAT_CURRENCY = "USD";
    private static final String VENDOR_VEZGO = "VEZGO";
    @Autowired
    private IUserAccountDAO userAccountDAO;
    @Autowired
    private SQSProducer sqsProducer;
    @Autowired
    private LambdaClient lambdaClient;

    @Override
    public EnumLambdaStatus syncUserAccounts(SyncUserAccountsFunctionReq request) {
        AccountNextStartingAfterKey nextStartingAfterKey = null;
        if (!Objects.isNull(request.getNextStartingAfter())) nextStartingAfterKey = request.getNextStartingAfter();
        Instant startedAt = Instant.now();

        do {
            if (Duration.between(startedAt, Instant.now()).toMinutes() > 13L) {
                this.lambdaClient.getLambdaClient().invoke(this.getInvokeRequest(request));
                return EnumLambdaStatus.SUCCESS;
            }
            UserAccounts response = this.userAccountDAO.getUserAccountPageByLastEvaluatedUser(nextStartingAfterKey);
            if (Objects.isNull(response) || response.getUserAccountList().isEmpty()) break;
            List<UserAccount> list = response.getUserAccountList();

            log.info("All connected accounts : {}", list);

            SyncUETxnAndAccountSQSRequest syncUETxnAndAccountSQSRequest = SyncUETxnAndAccountSQSRequest.builder()
                    .buildUserPortfolio(BUILD_PORTFOLIO)
                    .fiatCurrency(FIAT_CURRENCY)
                    .build();
            SyncVezgoUserAccountsSQSRequest syncVezgoUserAccountsSQSRequest = SyncVezgoUserAccountsSQSRequest.builder()
                    .fiatCurrency(FIAT_CURRENCY)
                    .build();
            list.forEach(userAccount -> {
                if (userAccount.getVendor().equals(VENDOR_VEZGO)) {
                    log.info("Producing message for VEZGO connected user : {} account : {}", userAccount.getUserId(), userAccount.getUniqueAccountName());
                    this.updateSyncVezgoUserAccountsSQSRequestMessage(userAccount.getUserId(), userAccount.getAccount(), userAccount.getUniqueId(), syncVezgoUserAccountsSQSRequest);
                    this.sqsProducer.produceMessage(EnumSQSQueue.SYNC_VEZGO_USER_ACCOUNTS_QUEUE, syncVezgoUserAccountsSQSRequest);
                    return;
                }
                userAccount.setSyncTxnStatus(EnumSyncTxnStatus.IN_PROGRESS);
                this.userAccountDAO.updateUserAccountStatus(userAccount);
                log.info("Producing message for DIRECT connected user : {} account : {}", userAccount.getUserId(), userAccount.getUniqueAccountName());
                this.updateSyncUETxnAndAccountSQSRequestMessage(userAccount.getUserId(), userAccount.getAccount(), userAccount.getUniqueId(), syncUETxnAndAccountSQSRequest);
                this.sqsProducer.produceMessage(EnumSQSQueue.SYNC_TRANSACTION_QUEUE, syncUETxnAndAccountSQSRequest);
            });

            AccountNextStartingAfterKey nextStartingAfter = response.getNextStartingAfter();
            nextStartingAfterKey = nextStartingAfter;
            request.setNextStartingAfter(nextStartingAfter);
        } while (nextStartingAfterKey != null);
        return EnumLambdaStatus.SUCCESS;
    }

    private void updateSyncUETxnAndAccountSQSRequestMessage(final Long userId, final String account, final String uniqueId, final SyncUETxnAndAccountSQSRequest syncUETxnAndAccountSQSRequest) {
        syncUETxnAndAccountSQSRequest.setUserId(userId);
        syncUETxnAndAccountSQSRequest.setAccount(account);
        syncUETxnAndAccountSQSRequest.setUniqueId(uniqueId);
    }

    private void updateSyncVezgoUserAccountsSQSRequestMessage(final Long userId, final String account, final String uniqueId, final SyncVezgoUserAccountsSQSRequest syncVezgoUserAccountsSQSRequest) {
        syncVezgoUserAccountsSQSRequest.setUserId(userId);
        syncVezgoUserAccountsSQSRequest.setAccount(account);
        syncVezgoUserAccountsSQSRequest.setUniqueId(uniqueId);
    }

    private InvokeRequest getInvokeRequest(final SyncUserAccountsFunctionReq request) {
        InvokeRequest invokeRequest = new InvokeRequest();
        invokeRequest.setFunctionName(EnumLambdaFunction.SYNC_USER_ACCOUNTS_FUNCTION.getFunctionName());
        invokeRequest.setPayload(MapperUtil.writeValuesAsString(request));
        return invokeRequest;
    }
}
