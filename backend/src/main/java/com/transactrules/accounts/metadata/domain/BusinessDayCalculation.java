package com.transactrules.accounts.metadata.domain;


import java.util.HashMap;
import java.util.Map;

public enum BusinessDayCalculation {
    AnyDay("ANY_DAY"),
    NextBusinessDay("NEXT_BUSINESS_DAY"),
    PreviousBusinessDay("PREVIOUS_BUSINESS_DAY"),
    ClosestBusinessDayOrNext("CLOSEST_BUSINESS_DAY_NEXT"),
    NextBusinessDayThisMonthOrPrevious("NEXT_BUSINESS_DAY_THIS_MONTH_OR_PREVIOUS");

    private final String value;

    BusinessDayCalculation(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    private static final Map<String, BusinessDayCalculation> lookup = new HashMap<>();

    static
    {
        for(BusinessDayCalculation item : BusinessDayCalculation.values())
        {
            lookup.put(item.value(), item);
        }
    }

    public static BusinessDayCalculation fromString(String value)
    {
        return lookup.get(value);
    }

}
