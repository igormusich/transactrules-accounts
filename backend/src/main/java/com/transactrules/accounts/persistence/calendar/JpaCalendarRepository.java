package com.transactrules.accounts.persistence.calendar;

import org.springframework.data.repository.CrudRepository;

public interface JpaCalendarRepository extends CrudRepository<CalendarDataObject,String> {
}
