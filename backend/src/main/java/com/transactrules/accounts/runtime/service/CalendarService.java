package com.transactrules.accounts.runtime.service;

import com.transactrules.accounts.runtime.domain.Calendar;

import java.util.List;

public interface CalendarService {
    List<Calendar> findAll();
    Calendar findByName(String name);
    Calendar getDefault();
}
