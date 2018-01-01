package com.transactrules.accounts.configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 11/27/2016.
 */
public enum ScheduleFrequency {
    Daily("DAILY"),
    Monthly("MONTHLY");

    private final String value;

    private ScheduleFrequency(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    private static final Map<String, ScheduleFrequency> lookup = new HashMap<>();

    static
    {
        for(ScheduleFrequency item : ScheduleFrequency.values())
        {
            lookup.put(item.value(), item);
        }
    }

    public static ScheduleFrequency fromString(String value)
    {
        return lookup.get(value);
    }
}
