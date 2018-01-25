package com.transactrules.accounts.web;

import com.transactrules.accounts.runtime.Account;

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

    public void setValuesFromAccount(Account account) {
        for(AmountControl control:amounts){
            if(account.getAmounts().containsKey(control.propertyName)){
                control.value = account.getAmounts().get(control.propertyName).getAmount();
            }
        }

        for(DateControl control:dates){
            if(account.getDates().containsKey(control.propertyName)){
                control.value = account.getDates().get(control.propertyName).getDate();
            }
        }

        for(OptionControl control:options){
            if(account.getOptions().containsKey(control.propertyName)){
                control.value = account.getOptions().get(control.propertyName);
            }
        }

        for(RateControl control:rates){
            if(account.getRates().containsKey(control.propertyName)){
                control.value = account.getRates().get(control.propertyName).getValue();
            }
        }

        for(InstalmentControl control:instalments){
            if(account.getInstalmentSets().containsKey(control.propertyName)){
                control.value = account.getInstalmentSets().get(control.propertyName);
            }
        }

        for(ScheduleControl control:schedules){
            if(account.getSchedules().containsKey(control.propertyName)){
                control.value = account.getSchedules().get(control.propertyName);
            }
        }
    }
}
