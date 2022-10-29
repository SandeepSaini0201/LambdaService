package com.crptm.lambdaservice.dao.interfaces;

import com.crptm.lambdaservice.pojo.AccountNextStartingAfterKey;
import com.crptm.lambdaservice.pojo.UserAccount;
import com.crptm.lambdaservice.pojo.UserAccounts;

public interface IUserAccountDAO {

    /* DAO method to fetch all direct integrated (DI) user accounts */
    UserAccounts getUserAccountPageByLastEvaluatedUser(final AccountNextStartingAfterKey nextStartingAfterKey);

    void updateUserAccountStatus(final UserAccount userAccount);
}
