package com.transactrules.accounts.runtime.service;

import com.transactrules.accounts.metadata.domain.AccountType;
import com.transactrules.accounts.runtime.domain.Transaction;
import com.transactrules.accounts.runtime.domain.TransactionSet;

import java.util.List;

public interface TransactionService {
    List<TransactionSet> save(AccountType accountType, String accountNumber, Transaction transaction);
    List<TransactionSet> save(AccountType accountType, String accountNumber, List<Transaction> transactions);

    TransactionSet getTransactionSet(String transactionSetId);
}
