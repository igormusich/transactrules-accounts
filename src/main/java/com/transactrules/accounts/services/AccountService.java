package com.transactrules.accounts.services;

import com.transactrules.accounts.metadata.AccountType;
import com.transactrules.accounts.runtime.Account;
import com.transactrules.accounts.runtime.Transaction;


import java.util.List;

public interface AccountService {
    Account create(AccountType accountType);
    Account save(Account account);
    List<Account> findAll();
    Account findByAccountNumber(String accountNumber);
    Transaction createTransaction(Transaction transaction) throws InterruptedException;
}
