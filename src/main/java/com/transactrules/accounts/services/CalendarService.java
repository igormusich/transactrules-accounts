package com.transactrules.accounts.services;

import com.transactrules.accounts.runtime.Calendar;

import java.util.List;

public interface CalendarService {
    List<Calendar> findAll();
    Calendar findByName(String name);
    Calendar getDefault();
}
