package com.transactrules.accounts;

import com.transactrules.accounts.configuration.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;


@Component
public class TestConfiguration {

    @Autowired
    private AccountTypeRepository accountTypeRepo;


    public void run() throws Exception {
        Logger logger = LoggerFactory.getLogger(TestConfiguration.class);

        logger.info("Saving default configuration (SavingsAccount)");

        AccountType savingsAccountType = createSavingsAccountType();

        accountTypeRepo.save(savingsAccountType);

        logger.info("Saved account type Id:" + savingsAccountType.getId().toString());

        logger.info("Default configuration saved (SavingsAccount)");

        logger.info("press any key ...");
    }

    public static AccountType createSavingsAccountType()
    {
        AccountType accountType = new AccountType( "SavingsAccount");

        PositionType currentPosition = accountType.addPositionType( "Current");
        PositionType interestAccruedPosition = accountType.addPositionType( "InterestAccrued" );

        TransactionType depositTransaction = accountType.addTransactionType("Deposit", false);

        depositTransaction.addRule(currentPosition, TransactionOperation.Add);

        TransactionType withdrawalTransaction = accountType.addTransactionType("Withdrawal",false);

        withdrawalTransaction.addRule(currentPosition, TransactionOperation.Subtract);

        TransactionType interestAccruedTransaction = accountType.addTransactionType("InterestAccrued", true);

        interestAccruedTransaction.addRule(interestAccruedPosition, TransactionOperation.Add);

        TransactionType interestCapitalizedTransaction = accountType.addTransactionType("InterestCapitalized", false);

        interestCapitalizedTransaction.addRule(interestAccruedPosition, TransactionOperation.Subtract );
        interestCapitalizedTransaction.addRule(currentPosition, TransactionOperation.Add );

        return accountType;
    }
}
