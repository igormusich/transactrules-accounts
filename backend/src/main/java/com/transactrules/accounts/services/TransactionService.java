package com.transactrules.accounts.services;

import com.transactrules.accounts.metadata.AccountType;
import com.transactrules.accounts.runtime.Transaction;
import com.transactrules.accounts.runtime.TransactionSet;

import java.util.List;

public interface TransactionService {
    List<TransactionSet> save(AccountType accountType, String accountNumber, Transaction transaction);
    List<TransactionSet> save(AccountType accountType, String accountNumber, List<Transaction> transactions);

    TransactionSet getTransactionSet(String transactionSetId);
}
