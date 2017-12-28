package com.transactrules.accounts.runtime;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.transactrules.accounts.runtime.accounts.Account;

import java.math.BigDecimal;

@DynamoDBDocument
public class AmountValue {

    private BigDecimal amount;

    public AmountValue (){

    }

    public AmountValue(BigDecimal value) {
        this.amount = value;
    }

    @DynamoDBAttribute
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
