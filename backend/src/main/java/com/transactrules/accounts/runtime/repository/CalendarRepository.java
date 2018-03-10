package com.transactrules.accounts.runtime.repository;

import com.transactrules.accounts.runtime.domain.Calendar;

public interface CalendarRepository {
    void save(Calendar calendar);
    Iterable<Calendar> findAll();
    Calendar findOne(String key);
}
