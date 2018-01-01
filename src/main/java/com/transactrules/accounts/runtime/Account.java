package com.transactrules.accounts.runtime;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.transactrules.accounts.configuration.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@DynamoDBTable(tableName = "Account")
public class Account {

    private String accountNumber;

    private boolean isActive;

    private String accountTypeName;

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
        this.accountTypeName = accountType.getName();
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
    public String getAccountTypeName() {
        return accountTypeName;
    }

    public void setAccountTypeName(String accountTypeName) {
        this.accountTypeName = accountTypeName;
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
            if(!positions.containsKey(positionType.getName())){
                initializePosition(positionType);
            }
        }

        for (DateType dateType: accountType.getDateTypes()) {
            if(!dates.containsKey(dateType.getName())){
                dates.put(dateType.getName(), new DateValue( LocalDate.now()));
            }
        }

        for (AmountType amountType: accountType.getAmountTypes()) {
            if(!amounts.containsKey(amountType.getName())){
                amounts.put(amountType.getName(), new AmountValue( new BigDecimal(0)));
            }
        }

        for (OptionType optionType: accountType.getOptionTypes()){
            if(!options.containsKey(optionType.getName())){
                options.put(optionType.getName(), new OptionValue());
            }
        }
    }


    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public DateValue initializeDate(DateType dateType, LocalDate date){
        DateValue dateValue;


        if(!dates.containsKey(dateType.getName())){
            dateValue = new DateValue( date);
            dates.put(dateType.getName(), dateValue);
        }
        else{
            dateValue = dates.get(dateType.getName());
        }

        return dateValue;
    }

    public Position initializePosition(PositionType positionType) {
        Position position = new Position(positionType);
        positions.put(positionType.getName(), position);
        return position;
    }

    public Schedule initializeSchedule(ScheduleType scheduleType){
        Schedule schedule = new Schedule(scheduleType);

        schedules.put(scheduleType.getName(), schedule);

        return schedule;
    }

}
