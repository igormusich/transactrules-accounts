package com.transactrules.accounts.services;

import com.transactrules.accounts.metadata.AccountType;

import java.util.List;

public interface AccountTypeService {
    void save(AccountType accountType);
    List<AccountType> findAll();
    AccountType findByClassName(String className);
    void deleteByClassName(String className);
}
