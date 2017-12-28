package com.transactrules.accounts.runtime;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.transactrules.accounts.configuration.PositionType;
import com.transactrules.accounts.configuration.TransactionOperation;

import java.math.BigDecimal;

@DynamoDBDocument
public class Position  {

    private BigDecimal amount;

    private String positionTypeId;

    public Position(){

    }

    public Position(PositionType positionType) {
        this.amount = BigDecimal.ZERO;
        this.positionTypeId = positionType.getId();
    }

    public void applyOperation(TransactionOperation operation, BigDecimal value){
        switch (operation) {
            case Subtract:
                amount = amount.subtract(value);
                break;
            case Add:
                amount = amount.add(value);
                break;
            default:
                break;
        }
    }

    public BigDecimal add(BigDecimal value) {
        BigDecimal result = amount.add(value);

        amount = value;

        return amount;
    }

    public BigDecimal subtract(BigDecimal value) {
        BigDecimal result = amount.subtract(value);

        amount = value;

        return amount;
    }

    @DynamoDBAttribute
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @DynamoDBAttribute
    public String getPositionTypeId() {
        return positionTypeId;
    }

    public void setPositionTypeId(String positionTypeId) {
        this.positionTypeId = positionTypeId;
    }
}
