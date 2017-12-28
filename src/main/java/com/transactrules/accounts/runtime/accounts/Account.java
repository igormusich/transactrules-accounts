package com.transactrules.accounts.runtime.accounts;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.transactrules.accounts.configuration.*;
import com.transactrules.accounts.runtime.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@DynamoDBTable(tableName = "Account")
public class Account {

    private String accountNumber;

    private boolean isActive;

    private String accountTypeId;

    private Map<String,Position> positions = new HashMap<>();

    private Map<String,DateValue> dates = new HashMap<>();

    private Map<String,AmountValue> amounts = new HashMap<>();

    private Map<String,OptionValue> options = new HashMap<>();

    private Map<String,Schedule> schedules = new HashMap<>();

    private List<Transaction> transactions = new ArrayList<>();

    public transient BusinessDayCalculator businessDayCalculator;

    public Account() {

    }

    public Account(AccountType accountType, String accountNumber){
        this.accountNumber = accountNumber;
        this.accountTypeId = accountType.getId();
    }

    @DynamoDBHashKey
    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @DynamoDBAttribute
    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @DynamoDBAttribute
    public String getAccountTypeId() {
        return accountTypeId;
    }

    public void setAccountTypeId(String accountTypeId) {
        this.accountTypeId = accountTypeId;
    }

    @DynamoDBAttribute
    public Map<String, Position> getPositions() {
        return positions;
    }

    public void setPositions(Map<String, Position> positions) {
        this.positions = positions;
    }

    @DynamoDBAttribute
    public Map<String, DateValue> getDates() {
        return dates;
    }

    public void setDates(Map<String, DateValue> dates) {
        this.dates = dates;
    }

    @DynamoDBAttribute
    public Map<String, AmountValue> getAmounts() {
        return amounts;
    }

    public void setAmounts(Map<String, AmountValue> amounts) {
        this.amounts = amounts;
    }

    @DynamoDBAttribute
    public Map<String, OptionValue> getOptions() {
        return options;
    }

    public void setOptions(Map<String, OptionValue> options) {
        this.options = options;
    }

    @DynamoDBAttribute
    public Map<String, Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(Map<String, Schedule> schedules) {
        this.schedules = schedules;
    }

    @DynamoDBAttribute
    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void initialize(AccountType accountType){
        for(PositionType positionType: accountType.getPositionTypes()){
            if(!positions.containsKey(positionType.getId())){
                initializePosition(positionType);
            }
        }

        for (DateType dateType: accountType.getDateTypes()) {
            if(!dates.containsKey(dateType.getId())){
                dates.put(dateType.getId(), new DateValue( LocalDate.MIN));
            }
        }

        for (AmountType amountType: accountType.getAmountTypes()) {
            if(!amounts.containsKey(amountType.getId())){
                amounts.put(amountType.getId(), new AmountValue( new BigDecimal(0)));
            }
        }

        for (OptionType optionType: accountType.getOptionTypes()){
            if(!options.containsKey(optionType.getId())){
                options.put(optionType.getId(), new OptionValue());
            }
        }
    }


    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public DateValue initializeDate(DateType dateType, LocalDate date){
        DateValue dateValue;


        if(!dates.containsKey(dateType.getId())){
            dateValue = new DateValue( date);
            dates.put(dateType.getId(), dateValue);
        }
        else{
            dateValue = dates.get(dateType.getId());
        }

        return dateValue;
    }

    public Position initializePosition(PositionType positionType) {
        Position position = new Position(positionType);
        positions.put(positionType.getId(), position);
        return position;
    }

    public Schedule initializeSchedule(ScheduleType scheduleType){
        Schedule schedule = new Schedule(scheduleType);

        schedules.put(scheduleType.getId(), schedule);

        return schedule;
    }

}
