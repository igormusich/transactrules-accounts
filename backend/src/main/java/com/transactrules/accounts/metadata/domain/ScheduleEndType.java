package com.transactrules.accounts.metadata.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 11/27/2016.
 */
public enum ScheduleEndType {
    NoEnd("NO_END"),
    EndDate("END_DATE"),
    Repeats("REPEATS");

    private final String value;

    ScheduleEndType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    private static final Map<String, ScheduleEndType> lookup = new HashMap<>();

    static
    {
        for(ScheduleEndType item : ScheduleEndType.values())
        {
            lookup.put(item.value(), item);
        }
    }

    public static ScheduleEndType fromString(String value)
    {
        return lookup.get(value);
    }

}
