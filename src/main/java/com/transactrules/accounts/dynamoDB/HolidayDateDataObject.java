package com.transactrules.accounts.dynamoDB;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.transactrules.accounts.runtime.HolidayDate;

import java.time.LocalDate;

@DynamoDBDocument
public class HolidayDateDataObject {

    private String description;
    private LocalDate value;

    public HolidayDateDataObject(){

    }

    public HolidayDateDataObject(HolidayDate holidayDate){
        this.description = holidayDate.getDescription();
        this.value = holidayDate.getValue();
    }

    public HolidayDate toHolidayDate(){
        return  new HolidayDate(this.description, this.value);
    }

    @DynamoDBAttribute
    public LocalDate getValue(){
        return value;
    }

    public void setValue(LocalDate value) {
        this.value = value;
    }

    @DynamoDBAttribute
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
