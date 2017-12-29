package com.transactrules.accounts.web;

import com.transactrules.accounts.configuration.*;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountTypeDTO {
    public String name;
    public List<PositionTypeDTO> positionTypes = new ArrayList<>();
    public List<DateTypeDTO> dateTypes = new ArrayList<>();
    public List<AmountTypeDTO> amountTypes = new ArrayList<>();
    public List<ScheduledTransaction> scheduledTransactions = new ArrayList<>();
    public List<TransactionTypeDTO> transactionTypes = new ArrayList<>();
    public List<ScheduleType> scheduleTypes = new ArrayList<>();


    public AccountType toAccountType(){

        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("The 'name' parameter must not be null or empty");
        }

        AccountType accountType = new AccountType( this.name);

        for (PositionTypeDTO postionType: this.positionTypes) {
            PositionType newPositionType = accountType.addPositionType(postionType.name);
        }

        for (DateTypeDTO dateType: this.dateTypes) {
            accountType.addDateType(dateType.name);
        }

        for (AmountTypeDTO amountType: this.amountTypes) {
            accountType.addAmountType(amountType.name, amountType.isValueDated);
        }

        for(TransactionTypeDTO transactionType :this.transactionTypes ){
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
