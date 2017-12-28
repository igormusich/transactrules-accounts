package com.transactrules.accounts.configuration;


public enum BusinessDayCalculation {
    AnyDay(1),
    NextBusinessDay(2),
    PreviousBusinessDay(3),
    ClosestBusinessDayOrNext(4),
    NextBusinessDayThisMonthOrPrevious(5);

    private final int value;

    BusinessDayCalculation(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static BusinessDayCalculation fromInteger(int x) {
        switch(x) {
            case 1:
                return AnyDay;
            case 2:
                return NextBusinessDay;
            case 3:
                return PreviousBusinessDay;
            case 4:
                return ClosestBusinessDayOrNext;
            case 5:
                return NextBusinessDayThisMonthOrPrevious;
        }
        return null;
    }


}
