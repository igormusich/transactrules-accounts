package com.transactrules.accounts.services;

import com.transactrules.accounts.runtime.Account;


import java.util.List;

public interface AccountService {
    Account create(String accountTypeName, String accountNumber);
    List<Account> findAll();
    Account findByAccountNumber(String accountNumber);
}
