package com.transactrules.accounts.runtime;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.transactrules.accounts.metadata.DateType;
import com.transactrules.accounts.metadata.PositionType;
import com.transactrules.accounts.metadata.ScheduleType;
import com.transactrules.accounts.metadata.ScheduledTransactionTiming;
import com.transactrules.accounts.utilities.*;

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

    private Map<String, InstalmentSet> instalmentSets = new HashMap<>();

    public transient BusinessDayCalculator businessDayCalculator;

    public transient LocalDate actionDate;
    public transient LocalDate valueDate;



    public Account() {

    }

    public Account(String accountNumber, String accountTypeName) {
        this.accountNumber = accountNumber;
        this.accountTypeName = accountTypeName;
    }

    public void initializeFromPrototype(Account prototype) {
        this.accountNumber = prototype.accountNumber;
        this.isActive = prototype.isActive;
        this.accountTypeName = prototype.accountTypeName;
        this.calendarNames = prototype.calendarNames;
        this.positions = prototype.positions;
        this.dates = prototype.dates;
        this.rates = prototype.rates;
        this.amounts = prototype.amounts;
        this.options = prototype.options;
        this.schedules = prototype.schedules;
        this.transactions = prototype.transactions;
        this.instalmentSets = prototype.instalmentSets;
        this.businessDayCalculator = prototype.businessDayCalculator;
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

    @DynamoDBIgnore
    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @DynamoDBTypeConverted(converter = InstalmentSetConverter.class)
    public Map<String, InstalmentSet> getInstalmentSets() {
        return instalmentSets;
    }

    public void setInstalmentSets(Map<String, InstalmentSet> instalmentSets) {
        this.instalmentSets = instalmentSets;
    }

    @DynamoDBAttribute
    public String[] getCalendarNames() {
        return calendarNames;
    }

    public void setCalendarNames(String[] calendarNames) {
        this.calendarNames = calendarNames;
    }

    private List<Transaction> snapshotTransactions;
    private Map<String, Position> snapshotPositions;

    public void setCalculated(){

    }


    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public DateValue initializeDate(DateType dateType, LocalDate date){

        DateValue dateValue;

        if(dates.containsKey(dateType.getPropertyName())){
            dateValue = dates.get(dateType.getPropertyName());
        }
        else
        {
            dateValue = new DateValue( date);
            dates.put(dateType.getPropertyName(), dateValue);
        }

        return dateValue;
    }


    public Position initializePosition(PositionType positionType) {
        Position position = new Position();
        positions.put(positionType.getPropertyName(), position);
        return position;
    }

    public Schedule initializeSchedule(ScheduleType scheduleType){
        Schedule schedule = new Schedule();

        schedule.businessDayCalculator = this.businessDayCalculator;

        schedules.put(scheduleType.getPropertyName(), schedule);

        return schedule;
    }


    public Transaction createTransaction(String transactionTypeName, BigDecimal amount) {

        Transaction transaction = new Transaction(this.getAccountNumber(), transactionTypeName,amount, actionDate, valueDate);

        processTransaction(transactionTypeName, amount);

        addTransaction(transaction);

        return transaction;
    }

    public Transaction createTransaction(Transaction transaction) {

        //TODO: implement backdating logic if transaction.valueDate < SystemParameters.actionDate

        processTransaction(transaction.getTransactionTypeName(), transaction.getAmount());

        addTransaction(transaction);

        return transaction;
    }


    public void processTransaction(String transactionTypeName, BigDecimal amount){

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

    public void forecast(LocalDate futureDate)
    {
        LocalDate originalValueDate = valueDate;


        if (!isActive)
        {
            startOfDay();
        }

        while (valueDate.isBefore(futureDate) || valueDate.isEqual(futureDate) )
        {
            endOfOfDay();

            valueDate = valueDate.plusDays(1);

            startOfDay();
        }

        valueDate = originalValueDate;
    }

    @JsonIgnore
    @DynamoDBIgnore
    public LocalDate ValueDate(){
        return valueDate;
    }

    public void setFutureInstalmentValue(String instalmentType, ScheduledTransactionTiming timing, BigDecimal value)
    {
        InstalmentSet instalmentSet = this.getInstalmentSets().get(instalmentType);

       for (LocalDate instalmentDate: instalmentSet.getInstalments().keySet())
        {
            InstalmentValue instalment = instalmentSet.getInstalments().get(instalmentDate);
            if (!instalment.getHasFixedValue())
            {
                if ( (timing == ScheduledTransactionTiming.StartOfDay && instalmentDate.isAfter(valueDate))
                        || (timing == ScheduledTransactionTiming.EndOfDay && (instalmentDate.isAfter(valueDate) || instalmentDate.isEqual(valueDate))))
                {
                    instalment.setAmount(value);
                }
            }
        }
    }

    public void snapshot() {
        snapshotPositions = positions;
        snapshotTransactions =  transactions;

        positions = new HashMap<>();

        for (String name:snapshotPositions.keySet())
        {
            positions.put(name, new Position());
        }

        transactions = new ArrayList<>();
    }

    public void restoreSnapshot() {
        positions = snapshotPositions;
        transactions = snapshotTransactions;
    }
}
