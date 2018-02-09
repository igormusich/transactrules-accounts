package com.transactrules.accounts.dynamoDB;

import com.transactrules.accounts.repository.CalendarRepository;
import com.transactrules.accounts.runtime.Calendar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CalendarRepositoryImpl implements CalendarRepository {
    @Autowired
    DynamoCalendarRepository dynamoRepository;

    @Override
    public void save(Calendar calendar) {
        CalendarDataObject calendarData = new CalendarDataObject(calendar);
        dynamoRepository.save(calendarData);
    }

    @Override
    public Iterable<Calendar> findAll() {

        List<Calendar> calendars = new ArrayList<>();

        for(CalendarDataObject  dataObject: dynamoRepository.findAll()){
            calendars.add(dataObject.toCalendar());
        }

        return calendars;
    }

    @Override
    public Calendar findOne(String key) {
        return dynamoRepository.findOne(key).toCalendar();
    }
}
