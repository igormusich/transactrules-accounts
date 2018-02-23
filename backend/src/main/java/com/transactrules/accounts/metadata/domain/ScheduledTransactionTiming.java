package com.transactrules.accounts.metadata.domain;

import java.util.HashMap;
import java.util.Map;

public enum ScheduledTransactionTiming {

    StartOfDay("START_OF_DAY"),
    EndOfDay("END_OF_DAY");

    private final String value;

    ScheduledTransactionTiming(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    private static final Map<String, ScheduledTransactionTiming> lookup = new HashMap<>();

    static
    {
        for(ScheduledTransactionTiming item : ScheduledTransactionTiming.values())
        {
            lookup.put(item.value(), item);
        }
    }

    public static ScheduledTransactionTiming fromString(String value)
    {
        return lookup.get(value);
    }

}
