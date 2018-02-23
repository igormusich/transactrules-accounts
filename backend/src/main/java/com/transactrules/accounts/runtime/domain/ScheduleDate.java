package com.transactrules.accounts.runtime.domain;

import java.time.LocalDate;

public class ScheduleDate
{
    public LocalDate value ;

    public ScheduleDate(){}

    public ScheduleDate(LocalDate value){
        this.value = value;
    }

    public LocalDate getValue() {
        return value;
    }

    public void setValue(LocalDate value) {
        this.value = value;
    }
}