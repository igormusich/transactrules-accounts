package com.transactrules.accounts.runtime.persistence.calendar;

import com.fasterxml.jackson.core.type.TypeReference;
import com.transactrules.accounts.config.ObjectMapperConfiguration;
import com.transactrules.accounts.runtime.domain.Calendar;
import com.transactrules.accounts.utilities.Utility;

import javax.persistence.*;
import java.io.IOException;

@Entity
@Table(name = "Calendar")
public class CalendarDataObject  {


    private String name;

    private String data;

    public CalendarDataObject(){

    }

    public CalendarDataObject(Calendar calendar){

        this.name = calendar.getName();
        setCalendar(calendar);
    }


    @Id
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Lob
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Transient
    public Calendar getCalendar() {

        Calendar object = null;
        try {
            object =  ObjectMapperConfiguration.getYamlObjectMapper().readValue(data, new TypeReference<Calendar>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }

        return object;
    }

    public void setCalendar(Calendar calendar) {

        this.data = Utility.getYaml(calendar);
    }
}




