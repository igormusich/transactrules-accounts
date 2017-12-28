package com.transactrules.accounts.configuration;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.transactrules.accounts.NamedAbstractEntity;


/**
 * Created by Administrator on 11/27/2016.
 */
@DynamoDBDocument
public class ScheduleType extends NamedAbstractEntity {


    private int scheduleFrequency;


    private int scheduleEndType;

    private String startDateExpression;

    private String endDateExpression;

    private String numberOfRepeatsExpression;

    private String intervalExpression;

    private Boolean isCalculated;

    public ScheduleType(){}

    public ScheduleType(
            String name,
            ScheduleFrequency frequency,
            ScheduleEndType endType,
            String startDateExpression,
            String endDateExpression,
            String numberOfRepeatsExpression,
            String intervalExpression,
            Boolean isCalculated) {
        super(name);
        this.scheduleFrequency = frequency.value();
        this.scheduleEndType = endType.value();
        this.startDateExpression = startDateExpression;
        this.endDateExpression = endDateExpression;
        this.numberOfRepeatsExpression = numberOfRepeatsExpression;
        this.intervalExpression = intervalExpression;
        this.isCalculated = isCalculated;
    }

    @DynamoDBAttribute
    public int getScheduleFrequency() {
        return scheduleFrequency;
    }

    public void setScheduleFrequency(int scheduleFrequency) {
        this.scheduleFrequency = scheduleFrequency;
    }

    @DynamoDBAttribute
    public int getScheduleEndType() {
        return scheduleEndType;
    }

    public void setScheduleEndType(int scheduleEndType) {
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
    public Boolean getCalculated() {
        return isCalculated;
    }

    public void setCalculated(Boolean calculated) {
        isCalculated = calculated;
    }

}
