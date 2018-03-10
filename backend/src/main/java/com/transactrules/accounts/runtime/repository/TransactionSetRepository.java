package com.transactrules.accounts.runtime.repository;

import com.transactrules.accounts.runtime.domain.TransactionSet;


public interface TransactionSetRepository  {
    void save(TransactionSet transactionSet);
    void save(Iterable<TransactionSet> transactionSet);
    TransactionSet findOne(String id);
}
