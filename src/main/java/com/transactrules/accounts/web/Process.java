package com.transactrules.accounts.web;

import java.util.ArrayList;
import java.util.List;

public class Process {
    public String accountNumber;


    public List<AmountElement> amounts = new ArrayList<>();
    public CalendarElement calendar;
    public List<DateElement>  dates = new ArrayList<>();
    public List<InstalmentElement> instalments = new ArrayList<>();
    public List<OptionElement> options = new ArrayList<>();
    public List<RateElement>  rates = new ArrayList<>();
    public List<ScheduleElement> schedules = new ArrayList<>();
}
