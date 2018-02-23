package com.transactrules.accounts.runtime.persistence.calendar;

import com.transactrules.accounts.repository.CalendarRepository;
import com.transactrules.accounts.runtime.domain.Calendar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CalendarRepositoryImpl implements CalendarRepository {
    @Autowired
    JpaCalendarRepository dynamoRepository;

    @Override
    public void save(Calendar calendar) {
        CalendarDataObject calendarData = new CalendarDataObject(calendar);
        dynamoRepository.save(calendarData);
    }

    @Override
    public Iterable<Calendar> findAll() {

        List<Calendar> calendars = new ArrayList<>();

        for (CalendarDataObject dataObject : dynamoRepository.findAll()) {
            calendars.add(dataObject.getCalendar());
        }

        return calendars;
    }

    @Override
    public Calendar findOne(String key) {

        CalendarDataObject dataObject = dynamoRepository.findOne(key);

        if (dataObject == null) {
            return null;
        }

        return dataObject.getCalendar();
    }
}
