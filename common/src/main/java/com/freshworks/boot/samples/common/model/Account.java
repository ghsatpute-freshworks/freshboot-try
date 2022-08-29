package com.freshworks.boot.samples.common.model;

import com.freshworks.boot.common.context.account.IAccount;
import lombok.AllArgsConstructor;
import lombok.Data;

// Ideally this will be stored in a data store.
@Data
@AllArgsConstructor
public class Account
        implements IAccount {
    private long accountId;

    @Override
    public long getAccountId() {
        return accountId;
    }
}
