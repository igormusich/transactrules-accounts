package com.transactrules.accounts.runtime;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.transactrules.accounts.utilities.LocalDateFormat;

import java.time.LocalDate;

@DynamoDBDocument
public class ScheduleDate
{
    public LocalDate value ;

    public ScheduleDate(){}

    public ScheduleDate(LocalDate value){
        this.value = value;
    }

    @DynamoDBAttribute
    @LocalDateFormat
    public LocalDate getValue() {
        return value;
    }

    public void setValue(LocalDate value) {
        this.value = value;
    }
}