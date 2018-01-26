package com.transactrules.accounts.runtime;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

import java.math.BigDecimal;
import java.time.LocalDate;

@DynamoDBDocument
public class AmountValue {

    private BigDecimal amount;

    private LocalDate valueDate;

    public AmountValue (){

    }

    public AmountValue(BigDecimal value){
        this.amount = value;
    }

    public AmountValue(BigDecimal value, LocalDate valueDate) {

        this.amount = value;
        this.valueDate = valueDate;
    }

    @DynamoDBAttribute
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @DynamoDBAttribute
    public LocalDate getValueDate() {
        return valueDate;
    }

    public void setValueDate(LocalDate valueDate) {
        this.valueDate = valueDate;
    }
}
