package com.transactrules.accounts.runtime.service;

import com.transactrules.accounts.metadata.domain.AccountType;
import com.transactrules.accounts.runtime.domain.Account;
import com.transactrules.accounts.runtime.domain.BusinessDayCalculator;
import com.transactrules.accounts.runtime.domain.Transaction;

import java.time.LocalDate;
import java.util.List;

public interface AccountService {
    Account toGeneratedAccount(Account account);
    Account toGeneratedAccount(Account account, BusinessDayCalculator calculator);
    Account toDataAccount(Account account);
    Account create(AccountType accountType);
    Account calculateProperties(Account account);
    Account calculateInstalments(Account account);
    Account  save(Account account);
    List<Account> findAll();
    Account findByAccountNumber(String accountNumber);
    Transaction createTransaction(String accountNumber, Transaction transaction) throws InterruptedException;

    Account activate(Account prototype);

    List<Transaction> findTransactions(String accountNumber, LocalDate fromDate, LocalDate toDate);

    List<Transaction> getTransactionTrace(String accountNumber, LocalDate fromDate, LocalDate toDate, List<String> positionTypes);

    void startOfDay();
    void endOfDay();
}
