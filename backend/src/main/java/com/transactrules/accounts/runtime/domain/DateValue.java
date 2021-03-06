package com.transactrules.accounts.runtime.domain;



import java.time.LocalDate;


public class DateValue{

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
