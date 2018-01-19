package com.transactrules.accounts.web;

import java.util.HashMap;
import java.util.Map;

public enum ProcessStatus {

    Pending("PENDING"),
    Completed("COMPLETED");

    private final String value;

    ProcessStatus(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    private static final Map<String, ProcessStatus> lookup = new HashMap<>();

    static
    {
        for(ProcessStatus item : ProcessStatus.values())
        {
            lookup.put(item.value(), item);
        }
    }

    public static ProcessStatus fromString(String value)
    {
        return lookup.get(value);
    }

}