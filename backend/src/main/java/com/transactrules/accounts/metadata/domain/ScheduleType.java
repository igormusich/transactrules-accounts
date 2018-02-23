package com.transactrules.accounts.metadata.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.transactrules.accounts.NamedAbstractEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;


/**
 * Created by Administrator on 11/27/2016.
 */
@ApiModel()
public class ScheduleType extends NamedAbstractEntity {


    @NotBlank
    @ApiModelProperty(dataType = "string", allowableValues = "DAILY, MONTHLY", value = "Schedule repeat period", notes = "Defines the recurrence period for the schedule")
    private String scheduleFrequency;

    @NotBlank
    @ApiModelProperty(dataType = "string", allowableValues = "NO_END, END_DATE, REPEATS", value = "Schedule end", notes = "Schedules can be open ended, end on specific date or after specified number of repeats")
    private String scheduleEndType;

    private String startDateExpression;

    private String endDateExpression;

    private String numberOfRepeatsExpression;

    private String intervalExpression;

    private String includeDatesExpression;

    private String excludeDatesExpression;

    @NotBlank
    @ApiModelProperty(dataType = "string", allowableValues ="ANY_DAY, NEXT_BUSINESS_DAY, PREVIOUS_BUSINESS_DAY , CLOSEST_BUSINESS_DAY_NEXT , NEXT_BUSINESS_DAY_THIS_MONTH_OR_PREVIOUS", value = "Define working day adjustment", notes ="In case when working day adjustments need to be applied determines how the adjustment needs to be applied")
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

    public String getScheduleFrequency() {
        return scheduleFrequency;
    }

    public void setScheduleFrequency(String scheduleFrequency) {
        this.scheduleFrequency = scheduleFrequency;
    }

    public String getScheduleEndType() {
        return scheduleEndType;
    }

    public void setScheduleEndType(String scheduleEndType) {
        this.scheduleEndType = scheduleEndType;
    }

    public String getStartDateExpression() {
        return startDateExpression;
    }

    public void setStartDateExpression(String startDateExpression) {
        this.startDateExpression = startDateExpression;
    }

    public String getEndDateExpression() {
        return endDateExpression;
    }

    public void setEndDateExpression(String endDateExpression) {
        this.endDateExpression = endDateExpression;
    }

    public String getNumberOfRepeatsExpression() {
        return numberOfRepeatsExpression;
    }

    public void setNumberOfRepeatsExpression(String numberOfRepeatsExpression) {
        this.numberOfRepeatsExpression = numberOfRepeatsExpression;
    }

    public String getIntervalExpression() {
        return intervalExpression;
    }

    public void setIntervalExpression(String intervalExpression) {
        this.intervalExpression = intervalExpression;
    }

    public Boolean getIsCalculated() {
        return isCalculated;
    }

    public void setIsCalculated(Boolean calculated) {
        isCalculated = calculated;
    }

    public String getBusinessDayCalculation() {
        return businessDayCalculation;
    }

    public void setBusinessDayCalculation(String businessDayCalculation) {
        this.businessDayCalculation = businessDayCalculation;
    }

    public String getIncludeDatesExpression() {
        return includeDatesExpression;
    }

    public void setIncludeDatesExpression(String includeDatesExpression) {
        this.includeDatesExpression = includeDatesExpression;
    }

    public String getExcludeDatesExpression() {
        return excludeDatesExpression;
    }

    public void setExcludeDatesExpression(String excludeDatesExpression) {
        this.excludeDatesExpression = excludeDatesExpression;
    }

    @JsonIgnore
    public Boolean getHasEndDate() {
        return scheduleEndType.equalsIgnoreCase(ScheduleEndType.EndDate.value());
    }

    @JsonIgnore
    public Boolean getHasStartDateExpression(){
        return (startDateExpression!=null && !startDateExpression.isEmpty());
    }

    @JsonIgnore
    public Boolean getHasEndDateExpression(){
        return (endDateExpression!=null && !endDateExpression.isEmpty());
    }

    @JsonIgnore
    public Boolean getHasIntervalExpression(){
        return (intervalExpression!=null && !intervalExpression.isEmpty());
    }

    @JsonIgnore
    public Boolean getHasNumberOfRepeatsExpression(){
        return (numberOfRepeatsExpression!=null && !numberOfRepeatsExpression.isEmpty());
    }

    @JsonIgnore
    public Boolean getHasIncludeDatesExpression(){
        return (includeDatesExpression!=null && !includeDatesExpression.isEmpty());
    }

    @JsonIgnore
    public Boolean getHasExcludeDatesExpression(){
        return (excludeDatesExpression!=null && !excludeDatesExpression.isEmpty());
    }


}
