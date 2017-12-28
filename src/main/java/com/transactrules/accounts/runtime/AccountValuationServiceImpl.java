package com.transactrules.accounts.runtime;

import com.transactrules.accounts.configuration.*;
import com.transactrules.accounts.runtime.accounts.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 313798977 on 2016/11/12.
 */


@Service
public class AccountValuationServiceImpl implements AccountValuationService {

    private AccountTypeRepository accountTypeRepository;
    private Account account;
    private AccountType accountType;
    private LocalDate actionDate;
    private LocalDate valueDate;

    @Autowired
    public AccountValuationServiceImpl(AccountTypeRepository accountTypeRepository){
        this.accountTypeRepository = accountTypeRepository;
    }

    @Override
    public void initialize(Account account) {
        this.account = account;
        accountType = accountTypeRepository.findOne(account.getAccountTypeId());
        actionDate = LocalDate.now();
        valueDate = LocalDate.now();

        account.initialize(accountType);
    }


    private void processTransaction(TransactionType transactionType, BigDecimal amount) {

        for (TransactionRuleType rule: transactionType.getTransactionRules()) {
            Position position = account.getPositions().get(rule.getPosititonTypeId());

            position.applyOperation(TransactionOperation.fromInteger( rule.getTransactionOperation()), amount);
        }
    }

    @Override
    public Transaction createTransaction(TransactionType transactionType, BigDecimal amount) {

        Transaction transaction = new Transaction(transactionType,amount,account, actionDate, valueDate);

        processTransaction(transactionType, amount);

        account.addTransaction(transaction);

        return transaction;
    }
}
