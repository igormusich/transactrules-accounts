package com.transactrules.accounts.web;

import com.transactrules.accounts.configuration.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountTypeDto {
    public String name;
    public List<PositionTypeDto> positionTypes = new ArrayList<>();
    public List<DateTypeDto> dateTypes = new ArrayList<>();
    public List<AmountTypeDto> amountTypes = new ArrayList<>();
    public List<ScheduledTransaction> scheduledTransactions = new ArrayList<>();
    public List<TransactionTypeDto> transactionTypes = new ArrayList<>();
    public List<ScheduleType> scheduleTypes = new ArrayList<>();


    public AccountType toAccountType(){

        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("The 'name' parameter must not be null or empty");
        }

        AccountType accountType = new AccountType( this.name);

        for (PositionTypeDto postionType: this.positionTypes) {
            PositionType newPositionType = accountType.addPositionType(postionType.name);
        }

        for (DateTypeDto dateType: this.dateTypes) {
            accountType.addDateType(dateType.name);
        }

        for (AmountTypeDto amountType: this.amountTypes) {
            accountType.addAmountType(amountType.name, amountType.isValueDated);
        }

        for(TransactionTypeDto transactionType :this.transactionTypes ){
            TransactionType newTransactionType= accountType.addTransactionType(transactionType.name, transactionType.maximumPrecision);

            for (String creditPositionTypeName: transactionType.creditPositionNames){

                Optional<PositionType>  creditPositionType = accountType.getPositionTypeByName(creditPositionTypeName);

                if(!creditPositionType.isPresent()){
                    throw new IllegalArgumentException(String.format("Credit position type with name %s is not defined", creditPositionTypeName));
                }

                newTransactionType.addRule(creditPositionType.get(), TransactionOperation.Add);
            }

            for (String debitPositionTypeName: transactionType.debitPositionNames){

                Optional<PositionType>  debitPositionType = accountType.getPositionTypeByName(debitPositionTypeName);

                if(!debitPositionType.isPresent()){
                    throw new IllegalArgumentException(String.format("Debit position type with name %s is not defined", debitPositionTypeName));
                }

                newTransactionType.addRule(debitPositionType.get(), TransactionOperation.Subtract);
            }
        }

        return accountType;
    }
}
