package com.transactrules.accounts.runtime;


import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;


public class DateValue{

    @NotNull
    private LocalDate date;

    public DateValue(){

    }

    public DateValue(LocalDate date) {
        this.date = date;
    }

    public LocalDate getDate(){
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Boolean isDue(LocalDate valueDate){
        return date.isEqual(valueDate);
    }

}