package com.transactrules.accounts.configuration;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.transactrules.accounts.NamedAbstractEntity;



@DynamoDBDocument
public class ScheduledTransaction extends NamedAbstractEntity {

    private String timing;

    private String scheduleTypeName;

    private String dateTypeName;

    private String transactionTypeId;

    private String amountExpression;

    private Integer sequence;

    public ScheduledTransaction(){

    }

   public ScheduledTransaction(
           String name,
           ScheduledTransactionTiming timing,
           DateType dateType,
           ScheduleType scheduleType,
           TransactionType transactionType,
           String amountExpression,
           Integer sequence
           ){

        super(name);
        this.timing = timing.value();

        if(dateType!=null)
            this.dateTypeName = dateType.getName();

        if(scheduleType!=null)
            this.scheduleTypeName = scheduleType.getName();

        this.transactionTypeId = transactionType.getName();
        this.amountExpression = amountExpression;
        this.sequence = sequence;
   }

   @DynamoDBAttribute
    public String getTiming() {
        return timing;
    }


    public void setTiming(String timing) {
        this.timing = timing;
    }

    @DynamoDBAttribute
    public String getScheduleTypeName() {
        return scheduleTypeName;
    }

    public void setScheduleTypeName(String scheduleTypeName) {
        this.scheduleTypeName = scheduleTypeName;
    }

    @DynamoDBAttribute
    public String getDateTypeName() {
        return dateTypeName;
    }

    public void setDateTypeName(String dateTypeName) {
        this.dateTypeName = dateTypeName;
    }

    @DynamoDBAttribute
    public String getTransactionTypeId() {
        return transactionTypeId;
    }

    public void setTransactionTypeId(String transactionTypeId) {
        this.transactionTypeId = transactionTypeId;
    }

    @DynamoDBAttribute
    public String getAmountExpression() {
        return amountExpression;
    }

    public void setAmountExpression(String amountExpression) {
        this.amountExpression = amountExpression;
    }

    @DynamoDBAttribute
    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }
}
