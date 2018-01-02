package com.transactrules.accounts.services;

import com.transactrules.accounts.metadata.AccountType;

import java.util.List;

public interface AccountTypeService {
    AccountType create(AccountType accountType);
    List<AccountType> findAll();
}
