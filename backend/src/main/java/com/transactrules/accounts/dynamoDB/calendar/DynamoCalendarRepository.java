package com.transactrules.accounts.dynamoDB.calendar;


import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface DynamoCalendarRepository extends CrudRepository<CalendarDataObject,String> {
}