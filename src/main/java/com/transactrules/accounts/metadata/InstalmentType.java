package com.transactrules.accounts.metadata;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.transactrules.accounts.NamedAbstractEntity;
import org.hibernate.validator.constraints.NotBlank;

@DynamoDBDocument
public class InstalmentType extends NamedAbstractEntity {

    @NotBlank
    private String timing; //ScheduledTransactionTiming
    @NotBlank
    private String scheduleTypeName;
    @NotBlank
    private String transactionTypeName;
    @NotBlank
    private String positionTypeName;
    //required for amortization calculations
    private String interestAccruedPositionTypeName;
    private String interestCapitalizedPositionTypeName;

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
    public String getScheduleTypeName() {
        return scheduleTypeName;
    }

    public void setScheduleTypeName(String scheduleTypeName) {
        this.scheduleTypeName = scheduleTypeName;
    }

    @DynamoDBAttribute
    public String getTransactionTypeName() {
        return transactionTypeName;
    }

    public void setTransactionTypeName(String transactionTypeName) {
        this.transactionTypeName = transactionTypeName;
    }

    @DynamoDBAttribute
    public String getPositionTypeName() {
        return positionTypeName;
    }

    public void setPositionTypeName(String positionTypeName) {
        this.positionTypeName = positionTypeName;
    }

    @DynamoDBAttribute
    public String getInterestAccruedPositionTypeName() {
        return interestAccruedPositionTypeName;
    }

    public void setInterestAccruedPositionTypeName(String interestAccruedPositionTypeName) {
        this.interestAccruedPositionTypeName = interestAccruedPositionTypeName;
    }

    @DynamoDBAttribute
    public String getInterestCapitalizedPositionTypeName() {
        return interestCapitalizedPositionTypeName;
    }

    public void setInterestCapitalizedPositionTypeName(String interestACapitalized) {
        this.interestCapitalizedPositionTypeName = interestACapitalized;
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
