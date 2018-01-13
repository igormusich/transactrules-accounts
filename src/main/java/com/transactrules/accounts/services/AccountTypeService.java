package com.transactrules.accounts.services;

import com.transactrules.accounts.metadata.AccountType;

import java.util.List;

public interface AccountTypeService {
    AccountType create(AccountType accountType);
    AccountType save(AccountType accountType);
    List<AccountType> findAll();
    AccountType findByClassName(String className);
}
