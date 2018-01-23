package com.transactrules.accounts.web;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "propertyType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ScheduleControl.class, name = "schedule"),
        @JsonSubTypes.Type(value = DateControl.class, name = "date"),
        @JsonSubTypes.Type(value = AmountControl.class, name = "amount"),
        @JsonSubTypes.Type(value = OptionControl.class, name = "option"),
        @JsonSubTypes.Type(value = CalendarControl.class, name = "calendar"),
        @JsonSubTypes.Type(value = InstalmentControl.class, name = "instalment"),
})
public class InputControl {
    public String propertyName;
    public String labelName;
    public boolean isRequired;
    public Integer order;

}
