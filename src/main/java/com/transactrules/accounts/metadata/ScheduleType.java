package com.transactrules.accounts.metadata;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.transactrules.accounts.NamedAbstractEntity;


/**
 * Created by Administrator on 11/27/2016.
 */
@DynamoDBDocument
public class ScheduleType extends NamedAbstractEntity {


    private String scheduleFrequency;

    private String scheduleEndType;

    private String startDateExpression;

    private String endDateExpression;

    private String numberOfRepeatsExpression;

    private String intervalExpression;

    private String businessDayCalculation;

    private Boolean isCalculated;

    public ScheduleType(){}

    public ScheduleType(
            String name,
            ScheduleFrequency frequency,
            ScheduleEndType endType,
            BusinessDayCalculation businessDayCalculation,
            String startDateExpression,
            String endDateExpression,
            String numberOfRepeatsExpression,
            String intervalExpression,
            Boolean isCalculated) {
        super(name);
        this.scheduleFrequency = frequency.value();
        this.scheduleEndType = endType.value();
        this.businessDayCalculation = businessDayCalculation.value();
        this.startDateExpression = startDateExpression;
        this.endDateExpression = endDateExpression;
        this.numberOfRepeatsExpression = numberOfRepeatsExpression;
        this.intervalExpression = intervalExpression;
        this.isCalculated = isCalculated;
    }

    @DynamoDBAttribute
    public String getScheduleFrequency() {
        return scheduleFrequency;
    }

    public void setScheduleFrequency(String scheduleFrequency) {
        this.scheduleFrequency = scheduleFrequency;
    }

    @DynamoDBAttribute
    public String getScheduleEndType() {
        return scheduleEndType;
    }

    public void setScheduleEndType(String scheduleEndType) {
        this.scheduleEndType = scheduleEndType;
    }

    @DynamoDBAttribute
    public String getStartDateExpression() {
        return startDateExpression;
    }

    public void setStartDateExpression(String startDateExpression) {
        this.startDateExpression = startDateExpression;
    }

    @DynamoDBAttribute
    public String getEndDateExpression() {
        return endDateExpression;
    }

    public void setEndDateExpression(String endDateExpression) {
        this.endDateExpression = endDateExpression;
    }

    @DynamoDBAttribute
    public String getNumberOfRepeatsExpression() {
        return numberOfRepeatsExpression;
    }

    public void setNumberOfRepeatsExpression(String numberOfRepeatsExpression) {
        this.numberOfRepeatsExpression = numberOfRepeatsExpression;
    }

    @DynamoDBAttribute
    public String getIntervalExpression() {
        return intervalExpression;
    }

    public void setIntervalExpression(String intervalExpression) {
        this.intervalExpression = intervalExpression;
    }

    @DynamoDBAttribute
    public Boolean getIsCalculated() {
        return isCalculated;
    }

    public void setIsCalculated(Boolean calculated) {
        isCalculated = calculated;
    }

    @DynamoDBAttribute
    public String getBusinessDayCalculation() {
        return businessDayCalculation;
    }

    public void setBusinessDayCalculation(String businessDayCalculation) {
        this.businessDayCalculation = businessDayCalculation;
    }

    @JsonIgnore
    @DynamoDBIgnore
    public Boolean getHasEndDate() {
        return scheduleEndType.equalsIgnoreCase(ScheduleEndType.EndDate.value());
    }

    @JsonIgnore
    @DynamoDBIgnore
    public Boolean getHasStartDateExpression(){
        return (startDateExpression!=null && !startDateExpression.isEmpty());
    }

    @JsonIgnore
    @DynamoDBIgnore
    public Boolean getHasEndDateExpression(){
        return (endDateExpression!=null && !endDateExpression.isEmpty());
    }

    @JsonIgnore
    @DynamoDBIgnore
    public Boolean getHasIntervalExpression(){
        return (intervalExpression!=null && !intervalExpression.isEmpty());
    }

    @JsonIgnore
    @DynamoDBIgnore
    public Boolean getHasNumberOfRepeatsExpression(){
        return (numberOfRepeatsExpression!=null && !numberOfRepeatsExpression.isEmpty());
    }

}
