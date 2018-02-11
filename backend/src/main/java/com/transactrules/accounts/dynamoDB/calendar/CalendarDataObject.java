package com.transactrules.accounts.dynamoDB.calendar;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.transactrules.accounts.runtime.Calendar;

@DynamoDBTable(tableName = "Calendar")
public class CalendarDataObject  {


    private String name;

    private Calendar calendar;

    public CalendarDataObject(){

    }

    public CalendarDataObject(Calendar calendar){

        this.name = calendar.getName();
        this.calendar = calendar;
    }


    @DynamoDBHashKey
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @DynamoDBTypeConverted(converter = CalendarDataConverter.class)
    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }
}




