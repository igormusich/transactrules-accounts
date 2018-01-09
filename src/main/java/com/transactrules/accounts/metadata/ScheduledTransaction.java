package com.transactrules.accounts.metadata;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.transactrules.accounts.NamedAbstractEntity;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;


@DynamoDBDocument
public class ScheduledTransaction extends NamedAbstractEntity {

    @NotBlank
    @ApiModelProperty(dataType = "string", allowableValues = "START_OF_DAY, END_OF_DAY", value = "Transaction timing", notes = "Controls if the scheduled transaction is applied in start of day or end of day operation")
    private String timing;

    private String scheduleTypeName;

    private String dateTypeName;

    @NotBlank
    private String transactionTypeName;

    @NotBlank
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
            this.dateTypeName = dateType.getPropertyName();

        if(scheduleType!=null)
            this.scheduleTypeName = scheduleType.getPropertyName();

        this.transactionTypeName = transactionType.getPropertyName();
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
    public String getTransactionTypeName() {
        return transactionTypeName;
    }

    public void setTransactionTypeName(String transactionTypeName) {
        this.transactionTypeName = transactionTypeName;
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

    @JsonIgnore
    @DynamoDBIgnore
    public Boolean getIsStartOfDay(){
        return timing.equalsIgnoreCase(ScheduledTransactionTiming.StartOfDay.value());
    }

    @JsonIgnore
    @DynamoDBIgnore
    public Boolean getIsEndOfDay(){
        return timing.equalsIgnoreCase(ScheduledTransactionTiming.EndOfDay.value());
    }

    @JsonIgnore
    @DynamoDBIgnore
    public Boolean getHasSchedule(){

        return (this.scheduleTypeName !=null && (!this.scheduleTypeName.isEmpty()));
    }

    @JsonIgnore
    @DynamoDBIgnore
    public Boolean getHasDate(){
        return (this.dateTypeName !=null && (!this.dateTypeName.isEmpty()));
    }

}
