package com.transactrules.accounts.runtime.domain;

import java.time.LocalDate;


public class HolidayDate  {

    private String description;
    private LocalDate value;

    public HolidayDate(){

    }

    public HolidayDate(String description, LocalDate value){
        this.description = description;
        this.value = value;
    }

    public LocalDate getValue(){
        return value;
    }

    public void setValue(LocalDate value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
