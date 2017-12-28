package com.transactrules.accounts.configuration;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.transactrules.accounts.NamedAbstractEntity;



@DynamoDBDocument
public class ScheduledTransaction extends NamedAbstractEntity {

    private int timing;

    private String scheduleTypeId;

    private String dateTypeId;

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
            this.dateTypeId = dateType.getId();

        if(scheduleType!=null)
            this.scheduleTypeId = scheduleType.getId();

        this.transactionTypeId = transactionType.getId();
        this.amountExpression = amountExpression;
        this.sequence = sequence;
   }

   @DynamoDBAttribute
    public int getTiming() {
        return timing;
    }


    public void setTiming(int timing) {
        this.timing = timing;
    }

    @DynamoDBAttribute
    public String getScheduleTypeId() {
        return scheduleTypeId;
    }

    public void setScheduleTypeId(String scheduleTypeId) {
        this.scheduleTypeId = scheduleTypeId;
    }

    @DynamoDBAttribute
    public String getDateTypeId() {
        return dateTypeId;
    }

    public void setDateTypeId(String dateTypeId) {
        this.dateTypeId = dateTypeId;
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
