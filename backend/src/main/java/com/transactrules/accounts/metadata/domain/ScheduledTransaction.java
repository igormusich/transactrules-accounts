package com.transactrules.accounts.metadata.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.transactrules.accounts.NamedAbstractEntity;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;


public class ScheduledTransaction extends NamedAbstractEntity {

    @NotEmpty
    @ApiModelProperty(dataType = "string", allowableValues = "START_OF_DAY, END_OF_DAY", value = "Transaction timing", notes = "Controls if the scheduled transaction is applied in start of day or end of day operation")
    private String timing;

    private String scheduleTypeName;

    private String dateTypeName;

    @NotEmpty
    private String transactionTypeName;

    @NotEmpty
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

    public String getTiming() {
        return timing;
    }


    public void setTiming(String timing) {
        this.timing = timing;
    }

    public String getScheduleTypeName() {
        return scheduleTypeName;
    }

    public void setScheduleTypeName(String scheduleTypeName) {
        this.scheduleTypeName = scheduleTypeName;
    }

    public String getDateTypeName() {
        return dateTypeName;
    }

    public void setDateTypeName(String dateTypeName) {
        this.dateTypeName = dateTypeName;
    }

    public String getTransactionTypeName() {
        return transactionTypeName;
    }

    public void setTransactionTypeName(String transactionTypeName) {
        this.transactionTypeName = transactionTypeName;
    }

    public String getAmountExpression() {
        return amountExpression;
    }

    public void setAmountExpression(String amountExpression) {
        this.amountExpression = amountExpression;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    @JsonIgnore
    public Boolean getIsStartOfDay(){
        return timing.equalsIgnoreCase(ScheduledTransactionTiming.StartOfDay.value());
    }

    @JsonIgnore
    public Boolean getIsEndOfDay(){
        return timing.equalsIgnoreCase(ScheduledTransactionTiming.EndOfDay.value());
    }

    @JsonIgnore
    public Boolean getHasSchedule(){

        return (this.scheduleTypeName !=null && (!this.scheduleTypeName.isEmpty()));
    }

    @JsonIgnore
    public Boolean getHasDate(){
        return (this.dateTypeName !=null && (!this.dateTypeName.isEmpty()));
    }

}
