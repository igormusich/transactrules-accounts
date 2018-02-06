package com.transactrules.accounts.services;

import com.transactrules.accounts.repository.CalendarRepository;
import com.transactrules.accounts.runtime.Calendar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class CalendarServiceImpl implements CalendarService {

    @Autowired
    CalendarRepository calendarRepository;
    @Override
    public List<Calendar> findAll() {
        Iterable<Calendar> items = calendarRepository.findAll();

        Iterator<Calendar> iterator = items.iterator();

        List<Calendar> list = new ArrayList<>();
        iterator.forEachRemaining(list::add);

        return list;
    }

    @Override
    public Calendar findByName(String name) {
        return calendarRepository.findOne(name);
    }

    @Override
    public Calendar getDefault() {
        List<Calendar> calendars = findAll();

        Optional<Calendar> optionalCalendar = calendars.stream().filter(c-> c.getDefault()==true).findFirst();

        if (!optionalCalendar.isPresent()){
            return null;
        }

        return  optionalCalendar.get();
    }


}
