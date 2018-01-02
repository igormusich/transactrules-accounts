package com.transactrules.accounts.runtime;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.transactrules.accounts.metadata.TransactionType;
import com.transactrules.accounts.utilities.LocalDateFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by 313798977 on 2016/11/11.
 */

@DynamoDBDocument
public class Transaction  {

    private String transactionTypeName;
    private BigDecimal amount;



    private String accountNumber;
    private LocalDate actionDate;
    private LocalDate valueDate;

    public Transaction(){

    }

    public Transaction(TransactionType transactionType, BigDecimal amount, Account account, LocalDate actionDate, LocalDate valueDate) {
        this.transactionTypeName = transactionType.getName();
        this.amount = amount;
        this.accountNumber = account.getAccountNumber();
        this.actionDate = actionDate;
        this.valueDate = valueDate;
    }

    @DynamoDBAttribute
    public String getTransactionTypeName() {
        return transactionTypeName;
    }

    public void setTransactionTypeName(String transactionTypeName) {
        this.transactionTypeName = transactionTypeName;
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
    @LocalDateFormat
    public LocalDate getActionDate() {
        return actionDate;
    }

    public void setActionDate(LocalDate actionDate) {
        this.actionDate = actionDate;
    }

    @DynamoDBAttribute
    @LocalDateFormat
    public LocalDate getValueDate() {
        return valueDate;
    }

    public void setValueDate(LocalDate valueDate) {
        this.valueDate = valueDate;
    }

}
