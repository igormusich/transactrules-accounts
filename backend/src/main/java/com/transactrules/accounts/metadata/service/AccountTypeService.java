package com.transactrules.accounts.metadata.service;

import com.transactrules.accounts.metadata.domain.AccountType;

import java.util.List;

public interface AccountTypeService {
    void save(AccountType accountType);
    List<AccountType> findAll();
    AccountType findByClassName(String className);
    void deleteByClassName(String className);
}
