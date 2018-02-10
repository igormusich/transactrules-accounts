package com.transactrules.accounts.repository;

import com.transactrules.accounts.runtime.TransactionSet;


public interface TransactionSetRepository  {
    void save(TransactionSet transactionSet);
    void save(Iterable<TransactionSet> transactionSet);
    TransactionSet findOne(String id);
}
