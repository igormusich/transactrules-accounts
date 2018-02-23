package com.transactrules.accounts.runtime.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountBuilder {

    //private CodeGenService codeGenService;

    private Class accountClass;

    private Logger logger = LoggerFactory.getLogger(AccountBuilder.class);

    private final String accountNumber;
    private final String accountTypeName;

    private  BusinessDayCalculator businessDayCalculator;

    private Map<String,Position> positions = new HashMap<>();

    private Map<String,DateValue> dates = new HashMap<>();

    private Map<String,RateValue> rates = new HashMap<>();

    private Map<String,AmountValue> amounts = new HashMap<>();

    private Map<String,OptionValue> options = new HashMap<>();

    private Map<String,Schedule> schedules = new HashMap<>();

    private Map<String,InstalmentSet> instalments = new HashMap<>();

    private List<String> calendarNames = new ArrayList<>();

    public AccountBuilder(Account prototype, Class accountClass){
        this.accountClass = accountClass;
        this.accountTypeName = prototype.getAccountTypeName();
        this.accountNumber = prototype.getAccountNumber();
        this.setProperties(prototype);
    }



    public AccountBuilder(String accountTypeName, String accountNumber, Class accountClass){
        this.accountTypeName = accountTypeName;
        this.accountNumber = accountNumber;
        this.accountClass = accountClass;

    }

    public AccountBuilder addDateValue(String name, DateValue dateValue){

        dates.put(name,dateValue);

        return this;
    }

    public AccountBuilder addDateValue(String name, LocalDate date){
        return addDateValue(name,new DateValue(date));
    }

    public AccountBuilder addAmountValue(String name,AmountValue amountValue){

        amounts.put(name,amountValue);

        return this;
    }

    public AccountBuilder addAmountValue(String name, BigDecimal amount, LocalDate valueDate){
        return addAmountValue(name,new AmountValue(amount,valueDate));
    }

    public AccountBuilder addAmountValue(String name, BigDecimal amount){
        return addAmountValue(name,new AmountValue(amount,null));
    }

    public AccountBuilder setBusinessDayCalculator(BusinessDayCalculator businessDayCalculator) {
        this.businessDayCalculator = businessDayCalculator;

        return this;
    }


    public AccountBuilder addOptionValue(String name, OptionValue optionValue){

        options.put(name,optionValue);

        return this;
    }

    public AccountBuilder  addOptionValue(String name, String value){
        return addOptionValue(name, new OptionValue(value));
    }

    public AccountBuilder addSchedule(String name, Schedule schedule){

        schedules.put(name,schedule);

        return this;
    }

    public AccountBuilder addRateValue(String name, RateValue rateValue) {

        rates.put(name, rateValue);

        return this;
    }

    public AccountBuilder addRateValue(String name, BigDecimal value, LocalDate valueDate) {

        return addRateValue(name,new RateValue(value,valueDate));
    }

    public AccountBuilder addCalendar(String name){
        calendarNames.add(name);
        return this;
    }

    public AccountBuilder setProperties(Account prototype) {
        Account account = getNewAccount();


        for(String calendarName: prototype.getCalendarNames()){
            addCalendar(calendarName);
        }

        for(String key: prototype.getAmounts().keySet()){
            addAmountValue(key, prototype.getAmounts().get(key));
        }

        for(String key: prototype.getDates().keySet()){
            addDateValue(key, prototype.getDates().get(key));
        }

        for(String key: prototype.getOptions().keySet()){
            addOptionValue(key, prototype.getOptions().get(key));
        }

        for(String key: prototype.getRates().keySet()){
            addRateValue(key, prototype.getRates().get(key));
        }

        for(String key: prototype.getSchedules().keySet()){
            addSchedule(key, prototype.getSchedules().get(key));
        }

        for(String key: prototype.getInstalmentSets().keySet()){
            addInstalmentSet(key, prototype.getInstalmentSets().get(key));
        }

        for(String key: prototype.getPositions().keySet()){
            addPosition(key, prototype.getPositions().get(key));
        }

        return this;
    }

    private void addPosition(String key, Position position) {
        this.positions.put(key, new Position(position));
    }

    private AccountBuilder addInstalmentSet(String key, InstalmentSet instalmentSet) {
        this.instalments.put(key, instalmentSet);

        return this;
    }

    public Account getAccount(){

        Account account = getNewAccount();

        account.setCalendarNames(calendarNames);
        account.setAccountNumber(accountNumber);
        account.setAccountTypeName(accountTypeName);
        account.setPositions(positions);
        account.setDates(dates);
        account.setRates(rates);
        account.setAmounts(amounts);
        account.setOptions(options);
        account.setSchedules(schedules);
        account.setInstalmentSets(instalments);
        account.businessDayCalculator = businessDayCalculator;

        account.setCalculated();
        return account;

    }

    public Account getNewAccount() {
        Account account = null;

        try {
            account = (Account) accountClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


        account.setAccountNumber(accountNumber);
        account.setAccountTypeName(accountTypeName);
        return account;
    }




}
