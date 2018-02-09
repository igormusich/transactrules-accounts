package com.transactrules.accounts.dynamoDB;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.transactrules.accounts.runtime.Calendar;
import com.transactrules.accounts.runtime.HolidayDate;

import java.util.ArrayList;
import java.util.List;

@DynamoDBTable(tableName = "Calendar")
public class CalendarDataObject  {


    private String name;

    private Boolean isDefault;

    private List<HolidayDateDataObject> holidays = new ArrayList<>();

    public CalendarDataObject(){

    }

    public CalendarDataObject(Calendar calendar){

        this.name = calendar.getName();
        this.isDefault = calendar.getDefault();
        for(HolidayDate date:calendar.getHolidays()){
            holidays.add(new HolidayDateDataObject(date));
        }
    }

    public Calendar toCalendar(){
        Calendar calendar = new Calendar(this.name, this.isDefault);

        for(HolidayDateDataObject dataObject: holidays){
            calendar.getHolidays().add(dataObject.toHolidayDate());
        }

        return calendar;
    }

    @DynamoDBHashKey
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @DynamoDBAttribute
    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    @DynamoDBTypeConverted(converter = HolidayDateListConverter.class)
    public List<HolidayDateDataObject> getHolidays() {
        return holidays;
    }

    public void setHolidays(List<HolidayDateDataObject> holidays) {
        this.holidays = holidays;
    }


}




