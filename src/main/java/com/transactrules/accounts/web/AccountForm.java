package com.transactrules.accounts.web;

import java.util.ArrayList;
import java.util.List;

public class AccountForm {

    public List<AmountControl> amounts = new ArrayList<>();
    public CalendarControl calendar;
    public List<DateControl>  dates = new ArrayList<>();
    public List<InstalmentControl> instalments = new ArrayList<>();
    public List<OptionControl> options = new ArrayList<>();
    public List<RateControl>  rates = new ArrayList<>();
    public List<ScheduleControl> schedules = new ArrayList<>();
}
