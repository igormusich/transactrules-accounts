package com.transactrules.accounts.repository;

import com.transactrules.accounts.runtime.Calendar;

public interface CalendarRepository {
    void save(Calendar calendar);
    Iterable<Calendar> findAll();
    Calendar findOne(String key);
}
