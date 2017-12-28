package com.transactrules.accounts.runtime;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

import java.time.LocalDate;

@DynamoDBDocument
public class DateValue{
    private LocalDate date;

    public DateValue(){

    }

    public DateValue( LocalDate date) {
        this.date = date;
    }

    @DynamoDBAttribute
    public LocalDate getDate(){
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
