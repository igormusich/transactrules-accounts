package com.transactrules.accounts.metadata;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 313798977 on 2016/11/12.
 */
public enum TransactionOperation {
    Add("ADD"),
    Subtract("SUBTRACT");

    @SuppressWarnings("unused")
    private String value;

    public String value(){
        return value;
    }

    TransactionOperation(String value) {
        this.value = value;
    }

    private static final Map<String, TransactionOperation> lookup = new HashMap<>();

    static
    {
        for(TransactionOperation item : TransactionOperation.values())
        {
            lookup.put(item.value(), item);
        }
    }

    public static TransactionOperation fromString(String value)
    {
        return lookup.get(value);
    }

}
