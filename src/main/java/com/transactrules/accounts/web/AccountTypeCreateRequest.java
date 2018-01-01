package com.transactrules.accounts.web;

import com.transactrules.accounts.configuration.*;
import org.hibernate.validator.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountTypeCreateRequest {
    @NotBlank
    private String name;
    private List<PositionTypeDto> positionTypes = new ArrayList<>();
    private List<DateTypeDto> dateTypes = new ArrayList<>();
    private List<AmountTypeDto> amountTypes = new ArrayList<>();
    private List<ScheduledTransaction> scheduledTransactions = new ArrayList<>();
    private List<TransactionTypeDto> transactionTypes = new ArrayList<>();
    public List<ScheduleType> scheduleTypes = new ArrayList<>();



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PositionTypeDto> getPositionTypes() {
        return positionTypes;
    }

    public void setPositionTypes(List<PositionTypeDto> positionTypes) {
        this.positionTypes = positionTypes;
    }

    public List<DateTypeDto> getDateTypes() {
        return dateTypes;
    }

    public void setDateTypes(List<DateTypeDto> dateTypes) {
        this.dateTypes = dateTypes;
    }

    public List<AmountTypeDto> getAmountTypes() {
        return amountTypes;
    }

    public void setAmountTypes(List<AmountTypeDto> amountTypes) {
        this.amountTypes = amountTypes;
    }

    public List<ScheduledTransaction> getScheduledTransactions() {
        return scheduledTransactions;
    }

    public void setScheduledTransactions(List<ScheduledTransaction> scheduledTransactions) {
        this.scheduledTransactions = scheduledTransactions;
    }

    public List<ScheduleType> getScheduleTypes() {
        return scheduleTypes;
    }

    public void setScheduleTypes(List<ScheduleType> scheduleTypes) {
        this.scheduleTypes = scheduleTypes;
    }


    public List<TransactionTypeDto> getTransactionTypes() {
        return transactionTypes;
    }

    public void setTransactionTypes(List<TransactionTypeDto> transactionTypes) {
        this.transactionTypes = transactionTypes;
    }

    public AccountType toAccountType() {


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
            TransactionType newTransactionType= accountType.addTransactionType(transactionType.getName(), transactionType.isMaximumPrecision());

            for (String creditPositionTypeName: transactionType.getCreditPositionNames()){

                Optional<PositionType>  creditPositionType = accountType.getPositionTypeByName(creditPositionTypeName);

                newTransactionType.addRule(creditPositionType.get(), TransactionOperation.Add);
            }

            for (String debitPositionTypeName: transactionType.getDebitPositionNames()){

                Optional<PositionType>  debitPositionType = accountType.getPositionTypeByName(debitPositionTypeName);


                newTransactionType.addRule(debitPositionType.get(), TransactionOperation.Subtract);

            }

        }

        return accountType;
    }
}
