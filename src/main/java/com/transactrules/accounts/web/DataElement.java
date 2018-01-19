package com.transactrules.accounts.web;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "propertyType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ScheduleElement.class, name = "schedule"),
        @JsonSubTypes.Type(value = DateElement.class, name = "date"),
        @JsonSubTypes.Type(value = AmountElement.class, name = "amount"),
        @JsonSubTypes.Type(value = OptionElement.class, name = "option"),
        @JsonSubTypes.Type(value = CalendarElement.class, name = "calendar"),
})
public class DataElement {
    public String propertyName;
    public String labelName;
    public boolean isRequired;
    public Integer order;

}
