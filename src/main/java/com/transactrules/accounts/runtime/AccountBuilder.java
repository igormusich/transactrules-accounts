package com.transactrules.accounts.runtime;

import com.transactrules.accounts.metadata.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountBuilder {

    private CodeGenService codeGenService;

    private Logger logger = LoggerFactory.getLogger(AccountBuilder.class);

    private final AccountType accountType;
    private final String accountNumber;
    private  BusinessDayCalculator businessDayCalculator;

    private Map<String,Position> positions = new HashMap<>();

    private Map<String,DateValue> dates = new HashMap<>();

    private Map<String,RateValue> rates = new HashMap<>();

    private Map<String,AmountValue> amounts = new HashMap<>();

    private Map<String,OptionValue> options = new HashMap<>();

    private Map<String,Schedule> schedules = new HashMap<>();

    private List<String> calendarNames = new ArrayList<>();

    public AccountBuilder(AccountType accountType, String accountNumber, CodeGenService codeGenService){
        this.accountType = accountType;
        this.accountNumber = accountNumber;
        this.codeGenService = codeGenService;

        for(PositionType positionType: accountType.getPositionTypes()){
            Position position = new Position();
            positions.put(positionType.getName(), position);
        }
    }

    public AccountBuilder addDateValue(String name, DateValue dateValue){
        DateType dateType = accountType.getDateTypes().stream().
                filter(dt->dt.getName().
                        equals(name)).
                findFirst().
                orElseThrow(() -> new IllegalArgumentException(String.format("%s is not valid date name for $s", name, accountType.getName()) ) );

        dates.put(name,dateValue);

        return this;
    }

    public AccountBuilder addDateValue(String name, LocalDate date){
        return addDateValue(name,new DateValue(date));
    }

    public AccountBuilder addAmountValue(String name,AmountValue amountValue){
        AmountType dateType = accountType.getAmountTypes().stream().
                filter(dt->dt.getName().
                        equals(name)).
                findFirst().
                orElseThrow(() -> new IllegalArgumentException(String.format("%s is not valid amount name for $s", name, accountType.getName()) ) );

        amounts.put(name,amountValue);

        return this;
    }

    public AccountBuilder addAmountValue(String name, BigDecimal amount, LocalDate valueDate){
        return addAmountValue(name,new AmountValue(amount,valueDate));
    }

    public AccountBuilder setBusinessDayCalculator(BusinessDayCalculator businessDayCalculator) {
        this.businessDayCalculator = businessDayCalculator;

        return this;
    }


    public AccountBuilder addOptionValue(String name, OptionValue optionValue){
        OptionType optionType = accountType.getOptionTypes().stream().
                filter(dt->dt.getName().
                        equals(name)).
                findFirst().
                orElseThrow(() -> new IllegalArgumentException(String.format("%s is not valid option name for $s", name, accountType.getName()) ) );

        options.put(name,optionValue);

        return this;
    }

    public AccountBuilder addOptionValue(String name, String value){

        return addOptionValue(name, new OptionValue(value));
    }

    public AccountBuilder addSchedule(String name, Schedule schedule){
        ScheduleType optionType = accountType.getScheduleTypes().stream().
                filter(dt->dt.getName().
                        equals(name)).
                findFirst().
                orElseThrow(() -> new IllegalArgumentException(String.format("%s is not valid schedule name for $s", name, accountType.getName()) ) );

        schedules.put(name,schedule);

        return this;
    }

    public AccountBuilder addRateValue(String name, RateValue rateValue) {
        RateType optionType = accountType.getRateTypes().stream().
                filter(dt->dt.getName().
                        equals(name)).
                findFirst().
                orElseThrow(() -> new IllegalArgumentException(String.format("%s is not valid rate name for $s", name, accountType.getName()) ) );

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

    public Account getAccount(){

        Account account = null;

        try{
            Class accountClass = codeGenService.getAccountClass(accountType);

            account = (Account) accountClass.newInstance();

        } catch (Exception ex){
            logger.error(ex.toString());
        }

        account.setAccountNumber(accountNumber);
        account.setAccountTypeName(accountType.getName());
        account.setPositions(positions);
        account.setDates(dates);
        account.setRates(rates);
        account.setAmounts(amounts);
        account.setOptions(options);
        account.setSchedules(schedules);
        account.businessDayCalculator = businessDayCalculator;

        account.setCalculated();

        return account;

    }


}
