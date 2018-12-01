package com.transactrules.accounts.metadata.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.transactrules.accounts.NamedAbstractEntity;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;

public class InstalmentType extends NamedAbstractEntity {

    @NotEmpty
    @ApiModelProperty(dataType = "string", allowableValues = "START_OF_DAY, END_OF_DAY", value = "Transaction timing", notes = "Controls if the scheduled transaction is applied in start of day or end of day operation")
    private String timing; //ScheduledTransactionTiming
    @NotEmpty
    private String scheduleTypeName;
    @NotEmpty
    private String transactionTypeName;
    @NotEmpty
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

    public String getTransactionTypeName() {
        return transactionTypeName;
    }

    public void setTransactionTypeName(String transactionTypeName) {
        this.transactionTypeName = transactionTypeName;
    }

    public String getPositionTypeName() {
        return positionTypeName;
    }

    public void setPositionTypeName(String positionTypeName) {
        this.positionTypeName = positionTypeName;
    }

    public String getInterestAccruedPositionTypeName() {
        return interestAccruedPositionTypeName;
    }

    public void setInterestAccruedPositionTypeName(String interestAccruedPositionTypeName) {
        this.interestAccruedPositionTypeName = interestAccruedPositionTypeName;
    }

    public String getInterestCapitalizedPositionTypeName() {
        return interestCapitalizedPositionTypeName;
    }

    public void setInterestCapitalizedPositionTypeName(String interestACapitalized) {
        this.interestCapitalizedPositionTypeName = interestACapitalized;
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
