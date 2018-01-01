package com.transactrules.accounts.runtime;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.transactrules.accounts.utilities.LocalDateFormat;

import java.time.LocalDate;

@DynamoDBDocument
public class DateValue{
    private LocalDate date;

    public DateValue(){

    }

    public DateValue( LocalDate date) {
        this.date = date;
    }

    @LocalDateFormat
    public LocalDate getDate(){
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
