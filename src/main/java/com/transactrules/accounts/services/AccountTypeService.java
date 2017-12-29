package com.transactrules.accounts.services;

import com.transactrules.accounts.configuration.AccountType;

import java.util.List;

public interface AccountTypeService {
    AccountType create(AccountType accountType);
    List<AccountType> findAll();
}
