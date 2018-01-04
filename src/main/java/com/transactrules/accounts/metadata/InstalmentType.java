package com.transactrules.accounts.metadata;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.transactrules.accounts.NamedAbstractEntity;

@DynamoDBDocument
public class InstalmentType extends NamedAbstractEntity {

    private String timing; //ScheduledTransactionTiming
    private String scheduleType;
    private String transactionType;
    private String positionType;
    //required for amortization calculations
    private String interestAccrued;
    private String interestCapitalized;

    public InstalmentType(){
        super();
    }

    public InstalmentType(String name){
        super(name);
    }

    @DynamoDBAttribute
    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    @DynamoDBAttribute
    public String getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(String scheduleType) {
        this.scheduleType = scheduleType;
    }

    @DynamoDBAttribute
    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    @DynamoDBAttribute
    public String getPositionType() {
        return positionType;
    }

    public void setPositionType(String positionType) {
        this.positionType = positionType;
    }

    @DynamoDBAttribute
    public String getInterestAccrued() {
        return interestAccrued;
    }

    public void setInterestAccrued(String interestAccrued) {
        this.interestAccrued = interestAccrued;
    }

    @DynamoDBAttribute
    public String getInterestCapitalized() {
        return interestCapitalized;
    }

    public void setInterestCapitalized(String interestACapitalized) {
        this.interestCapitalized = interestACapitalized;
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
    public String getTimingEnum(){

        String value = "";

        switch (ScheduledTransactionTiming.fromString(timing)){
            case StartOfDay:
                value = "StartOfDay";
                break;
            case EndOfDay:
                value ="EndOfDay";
                break;
            default:
        }
            return value;
    }

}