package com.transactrules.accounts.services;

import com.transactrules.accounts.runtime.Calendar;
import com.transactrules.accounts.runtime.CalendarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
}
