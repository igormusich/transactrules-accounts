package com.transactrules.accounts.services;

import com.transactrules.accounts.metadata.AccountType;
import com.transactrules.accounts.runtime.Account;
import com.transactrules.accounts.runtime.Transaction;


import java.time.LocalDate;
import java.util.List;

public interface AccountService {
    Account create(AccountType accountType);
    Account calculateProperties(Account account);
    Account calculateInstalments(Account account);
    Account save(Account account);
    List<Account> findAll();
    Account findByAccountNumber(String accountNumber);
    Transaction createTransaction(String accountNumber, Transaction transaction) throws InterruptedException;

    Account activate(Account prototype);

    List<Transaction> findTransactions(String accountNumber, LocalDate fromDate, LocalDate toDate);

    void startOfDay();
    void endOfDay();
}
