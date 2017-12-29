package com.transactrules.accounts.runtime;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.transactrules.accounts.configuration.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by 313798977 on 2016/11/11.
 */

@DynamoDBDocument
public class Transaction  {

    private String transactionTypeId;
    private BigDecimal amount;



    private String accountNumber;
    private LocalDate actionDate;
    private LocalDate valueDate;

    public Transaction(){

    }

    public Transaction(TransactionType transactionType, BigDecimal amount, Account account, LocalDate actionDate, LocalDate valueDate) {
        this.transactionTypeId = transactionType.getId();
        this.amount = amount;
        this.accountNumber = account.getAccountNumber();
        this.actionDate = actionDate;
        this.valueDate = valueDate;
    }

    @DynamoDBAttribute
    public String getTransactionTypeId() {
        return transactionTypeId;
    }

    public void setTransactionTypeId(String transactionTypeId) {
        this.transactionTypeId = transactionTypeId;
    }

    @DynamoDBAttribute
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @DynamoDBAttribute
    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @DynamoDBAttribute
    public LocalDate getActionDate() {
        return actionDate;
    }

    public void setActionDate(LocalDate actionDate) {
        this.actionDate = actionDate;
    }

    @DynamoDBAttribute
    public LocalDate getValueDate() {
        return valueDate;
    }

    public void setValueDate(LocalDate valueDate) {
        this.valueDate = valueDate;
    }

}