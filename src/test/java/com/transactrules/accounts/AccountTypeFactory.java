package com.transactrules.accounts;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transactrules.accounts.config.ObjectMapperConfiguration;
import com.transactrules.accounts.metadata.AccountType;
import com.transactrules.accounts.metadata.PositionType;
import com.transactrules.accounts.metadata.TransactionOperation;
import com.transactrules.accounts.metadata.TransactionType;

import java.io.IOException;


/**
 * Created by Administrator on 11/26/2016.
 */

public class AccountTypeFactory {


    public static AccountType createSavingsAccountType()
    {
        AccountType accountType = new AccountType( "SavingsAccount", "Savings Account");

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

    public static AccountType createLoanGivenAccountType(){
        java.net.URL url = AccountTypeFactory.class.getResource("/LoanGiven.yml");
        java.nio.file.Path resPath = null;
        String createAccountTypeYml =null;

        try {
            resPath = java.nio.file.Paths.get(url.toURI());
            createAccountTypeYml = new String(java.nio.file.Files.readAllBytes(resPath));
        } catch (Exception e) {
            e.printStackTrace();
        }

        ObjectMapper objectMapper = ObjectMapperConfiguration.getYamlObjectMapper();

        AccountType accountType= null;
        try {
            accountType = (AccountType) objectMapper.readValue(createAccountTypeYml, AccountType.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return accountType;
    }
}
