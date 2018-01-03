package com.transactrules.accounts.runtime;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.transactrules.accounts.metadata.*;
import com.transactrules.accounts.utilities.AmountValueMapConverter;
import com.transactrules.accounts.utilities.DateValueMapConverter;
import com.transactrules.accounts.utilities.RateValueMapConverter;
import com.transactrules.accounts.utilities.ScheduleMapConverter;

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

    private String[] calendarNames;

    private Map<String,Position> positions = new HashMap<>();

    private Map<String,DateValue> dates = new HashMap<>();

    private Map<String,RateValue> rates = new HashMap<>();

    private Map<String,AmountValue> amounts = new HashMap<>();

    private Map<String,OptionValue> options = new HashMap<>();

    private Map<String,Schedule> schedules = new HashMap<>();

    private List<Transaction> transactions = new ArrayList<>();

    public transient BusinessDayCalculator businessDayCalculator;

    public transient LocalDate actionDate;
    public transient LocalDate valueDate;

    public Account() {

    }

    public Account(String accountNumber, String accountTypeName) {
        this.accountNumber = accountNumber;
        this.accountTypeName = accountTypeName;
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

    @DynamoDBTypeConverted(converter = DateValueMapConverter.class)
    public Map<String, DateValue> getDates() {
        return dates;
    }

    public void setDates(Map<String,DateValue> dates) {
        this.dates = dates;
    }

    @DynamoDBTypeConverted(converter = RateValueMapConverter.class)
    public Map<String, RateValue> getRates() {
        return rates;
    }

    public void setRates(Map<String, RateValue> rates) {
        this.rates = rates;
    }

    @DynamoDBTypeConverted(converter = AmountValueMapConverter.class)
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

    @DynamoDBTypeConverted(converter = ScheduleMapConverter.class)
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

    @DynamoDBAttribute
    public String[] getCalendarNames() {
        return calendarNames;
    }

    public void setCalendarNames(String[] calendarNames) {
        this.calendarNames = calendarNames;
    }

    public void setCalculated(){

    }


    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public DateValue initializeDate(DateType dateType, LocalDate date){

        DateValue dateValue;

        if(dates.containsKey(dateType.getName())){
            dateValue = dates.get(dateType.getName());
        }
        else
        {
            dateValue = new DateValue( date);
            dates.put(dateType.getName(), dateValue);
        }

        return dateValue;
    }


    public Position initializePosition(PositionType positionType) {
        Position position = new Position();
        positions.put(positionType.getName(), position);
        return position;
    }

    public Schedule initializeSchedule(ScheduleType scheduleType){
        Schedule schedule = new Schedule(scheduleType);

        schedule.businessDayCalculator = this.businessDayCalculator;

        schedules.put(scheduleType.getName(), schedule);

        return schedule;
    }


    public Transaction createTransaction(TransactionType transactionType, BigDecimal amount) {

        Transaction transaction = new Transaction(transactionType,amount,this, actionDate, valueDate);

        processTransaction(transactionType, amount);

        addTransaction(transaction);

        return transaction;
    }

    public void processTransaction(TransactionType transactionType, BigDecimal amount) {

        for (TransactionRuleType rule: transactionType.getTransactionRules()) {
            Position position = getPositions().get(rule.getPosititonTypeName());

            position.applyOperation(TransactionOperation.fromString( rule.getTransactionOperation()), amount);
        }
    }

    public  void startOfDay(){

    }

    public  void endOfOfDay(){

    }
    public  String generatedAt(){
        return null;
    }
    public  void onDataChanged(){

    }
    public  void calculateInstaments(){

    }

}
