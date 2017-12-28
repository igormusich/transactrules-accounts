package com.transactrules.accounts.configuration;

/**
 * Created by Administrator on 11/27/2016.
 */
public enum ScheduleEndType {
    NoEnd(1),
    EndDate(2),
    Repeats(3);

    private final int value;

    ScheduleEndType(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static ScheduleEndType fromInteger(int x) {
        switch(x) {
            case 1:
                return NoEnd;
            case 2:
                return EndDate;
            case 3:
                return Repeats;
        }
        return null;
    }

}
