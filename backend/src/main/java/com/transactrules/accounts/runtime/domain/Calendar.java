package com.transactrules.accounts.runtime.domain;


import com.transactrules.accounts.metadata.domain.BusinessDayCalculation;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Calendar  implements BusinessDayCalculator {
    transient private Map<LocalDate, HolidayDate> holidaysMap;

    private String name;

    private Boolean isDefault;

    private List<HolidayDate> holidays = new ArrayList<>();

    public Calendar(){

    }

    public Calendar(String name, Boolean isDefault){

        this.name = name;
        this.isDefault = isDefault;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    public List<HolidayDate> getHolidays() {
        return holidays;
    }

    public void setHolidays(List<HolidayDate> holidays) {
        this.holidays = holidays;
    }

    public Calendar add(String descrption, LocalDate date){
        holidays.add(new HolidayDate(descrption, date));
        return this;
    }

    public Boolean IsBusinessDay(LocalDate date)
    {
        if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY )
        {
            return false;
        }

        return (holidaysMap().containsKey(date)==false);
    }

    protected Map<LocalDate, HolidayDate> holidaysMap()
    {

        if (holidaysMap== null)
        {
            holidaysMap = holidays.stream().collect(Collectors.toMap(HolidayDate::getValue, Function.identity())) ;
        }
        
        return holidaysMap;
    }

    public LocalDate GetCalculatedBusinessDay(LocalDate date, BusinessDayCalculation adjsutment)
    {
        if (adjsutment.equals(BusinessDayCalculation.AnyDay) )
        {
            return date;
        }

        if (adjsutment.equals(BusinessDayCalculation.PreviousBusinessDay))
        {
            return GetPreviousBusinessDay(date);
        }

        if (adjsutment.equals( BusinessDayCalculation.NextBusinessDay))
        {
            return GetNextBusinessDay(date);
        }

        LocalDate previousBusinessDay = GetPreviousBusinessDay(date);
        LocalDate nextBusinessDay = GetPreviousBusinessDay(date);

        if (adjsutment.equals(BusinessDayCalculation.ClosestBusinessDayOrNext))
        {
            if (Period.between(date,previousBusinessDay).getDays() > Period.between(nextBusinessDay,date).getDays())
            {
                return nextBusinessDay;
            }
            else if (Period.between(date,previousBusinessDay).getDays() < Period.between( nextBusinessDay,date).getDays())
            {
                return previousBusinessDay;
            }
            else {
                return nextBusinessDay;
            }
        }

        //last option is NextBusinessDayThisMonthOrPrevious

        if (nextBusinessDay.getMonth() == date.getMonth())
        {
            return nextBusinessDay;
        }

        return previousBusinessDay;

    }

    public LocalDate GetPreviousBusinessDay(LocalDate date)
    {
        while (!IsBusinessDay(date))
        {
            date = date.plusDays(-1);
        }

        return date;
    }

    public  LocalDate GetNextBusinessDay(LocalDate date)
    {
        while (!IsBusinessDay(date))
        {
            date = date.plusDays(1);
        }

        return date;
    }
}




