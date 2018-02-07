package com.transactrules.accounts.runtime;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.transactrules.accounts.utilities.LocalDateFormat;
import com.transactrules.accounts.utilities.PositionMapConverter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

/**
 * Created by 313798977 on 2016/11/11.
 */


public class Transaction  {

    private String transactionTypeName;
    private BigDecimal amount;
    private LocalDate actionDate;
    private LocalDate valueDate;
    private Map<String,BigDecimal> positions;

    public Transaction(){

    }

    public Transaction(String transactionTypeName, BigDecimal amount,  LocalDate actionDate, LocalDate valueDate) {

        this.transactionTypeName = transactionTypeName;
        this.amount = amount;
        this.actionDate = actionDate;
        this.valueDate = valueDate;
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

    @DynamoDBTypeConverted(converter = PositionMapConverter.class)
    public Map<String, BigDecimal> getPositions() {
        return positions;
    }

    public void setPositions(Map<String, BigDecimal> positions) {
        this.positions = positions;
    }
}
