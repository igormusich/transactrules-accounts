package com.transactrules.accounts.web;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "propertyType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ScheduleControl.class, name = "scheduleControl"),
        @JsonSubTypes.Type(value = DateControl.class, name = "dateControl"),
        @JsonSubTypes.Type(value = AmountControl.class, name = "amountControl"),
        @JsonSubTypes.Type(value = OptionControl.class, name = "optionControl"),
        @JsonSubTypes.Type(value = CalendarControl.class, name = "calendarControl"),
        @JsonSubTypes.Type(value = InstalmentControl.class, name = "instalmentControl"),
        @JsonSubTypes.Type(value = RateControl.class, name = "rateControl"),
})
public class InputControl {
    public String propertyName;
    public String labelName;
    public boolean isRequired;
    public Integer order;

}
