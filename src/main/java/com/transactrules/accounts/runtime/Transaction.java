package com.transactrules.accounts.runtime;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.transactrules.accounts.utilities.LocalDateFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by 313798977 on 2016/11/11.
 */

@DynamoDBTable(tableName = "Transaction")
public class Transaction  {

    private String accountNumber;
    private String transactionTypeName;
    private BigDecimal amount;
    private LocalDate actionDate;
    private LocalDate valueDate;

    public Transaction(){

    }

    public Transaction(String accountNumber, String transactionTypeName, BigDecimal amount,  LocalDate actionDate, LocalDate valueDate) {
        this.accountNumber = accountNumber;
        this.transactionTypeName = transactionTypeName;
        this.amount = amount;
        this.accountNumber = accountNumber;
        this.actionDate = actionDate;
        this.valueDate = valueDate;
    }

    @DynamoDBHashKey
    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    //@DynamoDBRangeKey(attributeName = "ActionDate")
    @LocalDateFormat
    public LocalDate getActionDate() {
        return actionDate;
    }

    public void setActionDate(LocalDate actionDate) {
        this.actionDate = actionDate;
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
    @LocalDateFormat
    public LocalDate getValueDate() {
        return valueDate;
    }

    public void setValueDate(LocalDate valueDate) {
        this.valueDate = valueDate;
    }

}
