package com.transactrules.accounts.runtime.service;

import com.transactrules.accounts.repository.CalendarRepository;
import com.transactrules.accounts.runtime.domain.Calendar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CalendarServiceImpl implements CalendarService {

    @Autowired
    CalendarRepository calendarRepository;
    @Override
    public List<Calendar> findAll() {
        Iterable<Calendar> items = calendarRepository.findAll();

        ArrayList<Calendar> list = new ArrayList<>();

        for(Calendar calendar:items){
            list.add(calendar);
        }


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
