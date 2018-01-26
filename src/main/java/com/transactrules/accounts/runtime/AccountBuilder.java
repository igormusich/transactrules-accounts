package com.transactrules.accounts.runtime;

import com.transactrules.accounts.metadata.*;
import com.transactrules.accounts.services.AccountTypeService;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
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

    private Map<String,InstalmentSet> instalments = new HashMap<>();

    private List<String> calendarNames = new ArrayList<>();


    public AccountBuilder(Account prototype, AccountTypeService accountTypeService, CodeGenService codeGenService){
        this.codeGenService = codeGenService;
        this.accountType = accountTypeService.findByClassName(prototype.getAccountTypeName());
        this.accountNumber = prototype.getAccountNumber();
        this.setProperties(prototype);
    }


    public AccountBuilder(AccountType accountType, String accountNumber, CodeGenService codeGenService){
        this.accountType = accountType;
        this.accountNumber = accountNumber;
        this.codeGenService = codeGenService;

        for(PositionType positionType: accountType.getPositionTypes()){
            Position position = new Position();
            positions.put(positionType.getPropertyName(), position);
        }
    }

    public AccountBuilder addDateValue(String name, DateValue dateValue){
        DateType dateType = accountType.getDateTypes().stream().
                filter(dt->dt.getPropertyName().
                        equals(name)).
                findFirst().
                orElseThrow(() -> new IllegalArgumentException(String.format("%s is not valid date propertyName for $s", name, accountType.getClassName()) ) );

        dates.put(name,dateValue);

        return this;
    }

    public AccountBuilder addDateValue(String name, LocalDate date){
        return addDateValue(name,new DateValue(date));
    }

    public AccountBuilder addAmountValue(String name,AmountValue amountValue){
        AmountType dateType = accountType.getAmountTypes().stream().
                filter(dt->dt.getPropertyName().
                        equals(name)).
                findFirst().
                orElseThrow(() -> new IllegalArgumentException(String.format("%s is not valid amount propertyName for $s", name, accountType.getClassName()) ) );

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
        OptionType optionType = accountType.getOptionTypes().stream().
                filter(dt->dt.getPropertyName().
                        equals(name)).
                findFirst().
                orElseThrow(() -> new IllegalArgumentException(String.format("%s is not valid option propertyName for $s", name, accountType.getClassName()) ) );

        options.put(name,optionValue);

        return this;
    }

    public AccountBuilder  addOptionValue(String name, String value){
        return addOptionValue(name, new OptionValue(value));
    }

    public AccountBuilder addSchedule(String name, Schedule schedule){
        ScheduleType optionType = accountType.getScheduleTypes().stream().
                filter(dt->dt.getPropertyName().
                        equals(name)).
                findFirst().
                orElseThrow(() -> new IllegalArgumentException(String.format("%s is not valid schedule propertyName for $s", name, accountType.getClassName()) ) );

        schedules.put(name,schedule);

        return this;
    }

    public AccountBuilder addRateValue(String name, RateValue rateValue) {
        RateType optionType = accountType.getRateTypes().stream().
                filter(dt->dt.getPropertyName().
                        equals(name)).
                findFirst().
                orElseThrow(() -> new IllegalArgumentException(String.format("%s is not valid rate propertyName for $s", name, accountType.getClassName()) ) );

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

        return this;
    }

    private AccountBuilder addInstalmentSet(String key, InstalmentSet instalmentSet) {
        this.instalments.put(key, instalmentSet);

        return this;
    }

    public Account getAccount(){

        Account account = getNewAccount();

        account.setCalendarNames(calendarNames);
        account.setAccountNumber(accountNumber);
        account.setAccountTypeName(accountType.getClassName());
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

    @Nullable
    //
    public Account getNewAccount() {
        Account account = null;

        try{
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            Class accountClass = codeGenService.getAccountClass(accountType, new PrintWriter(os));

            account = (Account) accountClass.newInstance();

        } catch (Exception ex){
            logger.error(ex.toString());
        }

        account.setAccountTypeName(accountType.getClassName());
        return account;
    }


}
