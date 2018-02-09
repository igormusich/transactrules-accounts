package com.transactrules.accounts.repository;

import com.transactrules.accounts.metadata.AccountType;

import java.util.List;

public interface AccountTypeRepository {
    void save(AccountType accountType);
    List<AccountType> findAll();
    AccountType findOne(String key);
    void delete(String key);
    boolean exists(String key);
}
