package com.transactrules.accounts.configuration;

/**
 * Created by Administrator on 11/27/2016.
 */
public enum ScheduleFrequency {
    Daily(1),
    Monthly(2);

    private final int value;

    private ScheduleFrequency(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static ScheduleFrequency fromInteger(int x) {
        switch(x) {
            case 1:
                return Daily;
            case 2:
                return Monthly;
        }
        return null;
    }


}
