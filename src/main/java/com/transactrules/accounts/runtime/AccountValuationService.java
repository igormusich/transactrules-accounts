package com.transactrules.accounts.runtime;

import com.transactrules.accounts.metadata.TransactionType;

import java.math.BigDecimal;

/**
 * Created by 313798977 on 2016/11/12.
 */
public interface AccountValuationService {

    void initialize(Account account);

    Transaction createTransaction(TransactionType transactionType, BigDecimal amount);

}
