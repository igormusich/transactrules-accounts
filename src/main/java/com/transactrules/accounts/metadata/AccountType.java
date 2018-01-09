package com.transactrules.accounts.metadata;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import io.swagger.annotations.ApiModel;
import org.hibernate.validator.constraints.NotBlank;
import org.joda.time.LocalDateTime;

import javax.validation.Valid;
import java.util.*;
import java.util.function.Function;

/**
 * Aggregate root for account metadata
 */

@ApiModel
@DynamoDBTable(tableName = "AccountType")
public class AccountType {

    @NotBlank
    private String className;

    @NotBlank
    private String labelName;

    @Valid
    private List<PositionType> positionTypes = new ArrayList<>();

    @Valid
    private List<DateType> dateTypes = new ArrayList<>();

    @Valid
    private List<AmountType> amountTypes = new ArrayList<>();

    @Valid
    private List<RateType> rateTypes = new ArrayList<>();

    @Valid
    private List<OptionType> optionTypes = new ArrayList<>();

    @Valid
    private List<ScheduledTransaction> scheduledTransactions = new ArrayList<>();

    @Valid
    private List<TransactionType> transactionTypes = new ArrayList<>();

    @Valid
    private List<ScheduleType> scheduleTypes = new ArrayList<>();

    @Valid
    private List<InstalmentType> instalmentTypes = new ArrayList<>();

    public AccountType() {

    }

    public AccountType (String className, String labelName) {
        this.className = className;
        this.labelName = labelName;
    }


    @DynamoDBHashKey(attributeName = "ClassName")
    public String getClassName(){
        return this.className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @DynamoDBAttribute(attributeName = "LabelName")
    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    @DynamoDBAttribute(attributeName = "PositionTypes")
    public List<PositionType> getPositionTypes() {
        return positionTypes;
    }

    public void setPositionTypes(List<PositionType> positionTypes) {
        this.positionTypes = positionTypes;
    }

    @DynamoDBAttribute(attributeName = "TransactionTypes")
    public List<TransactionType> getTransactionTypes() {

        return transactionTypes;
    }

    public void setTransactionTypes(List<TransactionType> transactionTypes) {
        this.transactionTypes = transactionTypes;
    }

    @DynamoDBAttribute(attributeName = "DateTypes")
    public List<DateType> getDateTypes(){
        return dateTypes;
    }

    public void setDateTypes(List<DateType> dateTypes) {
        this.dateTypes = dateTypes;
    }

    @DynamoDBAttribute(attributeName = "AmountTypes")
    public List<AmountType> getAmountTypes(){
        return amountTypes;
    }

    public void setAmountTypes(List<AmountType> amountTypes) {
        this.amountTypes = amountTypes;
    }

    @DynamoDBAttribute(attributeName = "OptionTypes")
    public List<OptionType> getOptionTypes(){
        return optionTypes;
    }

    public void setOptionTypes(List<OptionType> optionTypes) {
        this.optionTypes = optionTypes;
    }

    @DynamoDBAttribute(attributeName = "RateTypes")
    public List<RateType> getRateTypes() {
        return rateTypes;
    }

    public void setRateTypes(List<RateType> rateTypes) {
        this.rateTypes = rateTypes;
    }

    @DynamoDBAttribute(attributeName = "ScheduledTransactions")
    public List<ScheduledTransaction> getScheduledTransactions() {
        Collections.sort( scheduledTransactions, Comparator.comparing(ScheduledTransaction::getSequence));
        return scheduledTransactions;
    }

    public void setScheduledTransactions(List<ScheduledTransaction> scheduledTransactions) {
        this.scheduledTransactions = scheduledTransactions;
    }

    @DynamoDBAttribute(attributeName = "ScheduleTypes")
    public List<ScheduleType> getScheduleTypes() {
        return scheduleTypes;
    }

    public void setScheduleTypes(List<ScheduleType> scheduleTypes) {
        this.scheduleTypes = scheduleTypes;
    }

    @DynamoDBAttribute(attributeName = "InstalmentTypes")
    public List<InstalmentType> getInstalmentTypes() {
        return instalmentTypes;
    }

    public void setInstalmentTypes(List<InstalmentType> instalmentTypes) {
        this.instalmentTypes = instalmentTypes;
    }

    public Optional<TransactionType> getTransactionType(String transactionTypeName){

        for(TransactionType transactionType : transactionTypes) {
           if(transactionType.getPropertyName().equalsIgnoreCase(transactionTypeName)){
               return Optional.of(transactionType);
           }
        }

        return Optional.empty();
    }

    public DateType addDateType(String name){
        DateType dateType = new DateType(name);
        dateTypes.add(dateType);
        return dateType;
    }

    public PositionType addPositionType(String name){
        PositionType positionType = new PositionType( name);
        positionTypes.add(positionType);
        return positionType;
    }

    public TransactionType addTransactionType(String name, boolean hasMaximumPrecission){
        TransactionType transactionType = new TransactionType(name, hasMaximumPrecission);
        transactionTypes.add(transactionType);

        return transactionType;
    }

    public TransactionType addTransactionType(String name){
        TransactionType transactionType = new TransactionType(name, false);
        transactionTypes.add(transactionType);

        return transactionType;
    }

    public ScheduleType addCalculatedScheduleType(String name,
                                                  ScheduleFrequency frequency,
                                                  ScheduleEndType endType,
                                                  BusinessDayCalculation businessDayCalculation,
                                                  String startDateExpression,
                                                  String endDateExpression,
                                                  String numberOfRepeatsExpression,
                                                  String intervalExpression){

        ScheduleType scheduleType = new ScheduleType(name, frequency,endType,businessDayCalculation, startDateExpression, endDateExpression,numberOfRepeatsExpression,intervalExpression,true);

        scheduleTypes.add(scheduleType);

        return  scheduleType;
    }

    public ScheduleType addUserInputScheduleType(String name,
                                                 ScheduleFrequency frequency,
                                                 ScheduleEndType endType,
                                                 BusinessDayCalculation businessDayCalculation,
                                                 String startDateExpression,
                                                 String endDateExpression,
                                                 String numberOfRepeatsExpression,
                                                 String intervalExpression){

        ScheduleType scheduleType = new ScheduleType( name, frequency,endType,businessDayCalculation, startDateExpression, endDateExpression,numberOfRepeatsExpression,intervalExpression,false);

        scheduleTypes.add(scheduleType);

        return  scheduleType;
    }

    public InstalmentType addInstalmentType(String name, ScheduledTransactionTiming timing, String scheduleType, String transactionType, String positionType, String interestAccrued, String interestCapitalized) {
        InstalmentType instalmentType = new InstalmentType(name);
        instalmentType.setTiming(timing.value());
        instalmentType.setScheduleTypeName(scheduleType);
        instalmentType.setTransactionTypeName(transactionType);
        instalmentType.setPositionTypeName( positionType);
        instalmentType.setInterestAccruedPositionTypeName(interestAccrued);
        instalmentType.setInterestCapitalizedPositionTypeName(interestCapitalized);

        instalmentTypes.add(instalmentType);

        return instalmentType;
    }


    public Optional<PositionType> getPositionTypeByName(String name){
        return positionTypes.stream().filter(pt -> pt.getPropertyName().equalsIgnoreCase(name)).findFirst();
    }

    public Optional<DateType> getDateTypeByName(String name){
        return dateTypes.stream().filter(dt -> dt.getPropertyName().equalsIgnoreCase(name)).findFirst();
    }

    public Optional<TransactionType> getTransactionTypeByName(String name){
        return transactionTypes.stream().filter(tt->tt.getPropertyName().equalsIgnoreCase(name)).findFirst();
    }

    public Optional<AmountType> getAmountTypeByName(String name) {
        return amountTypes.stream().filter(tt->tt.getPropertyName().equalsIgnoreCase(name)).findFirst();
    }

    public Optional<OptionType> getOptionTypeByName(String name) {
        return optionTypes.stream().filter(tt->tt.getPropertyName().equalsIgnoreCase(name)).findFirst();
    }


    public AmountType addAmountType(String name, Boolean isValueDated) {
        AmountType amountType = new AmountType(name, isValueDated);
        amountTypes.add(amountType);
        return amountType;
    }

    public RateType addRateType(String name){
        RateType rateType = new RateType( name);
        rateTypes.add(rateType);
        return rateType;
    }

    public OptionType addOptionType(String name, String optionListExpression){
        OptionType optionType = new OptionType( name, optionListExpression);
        optionTypes.add(optionType);
        return  optionType;
    }

    public ScheduledTransaction addDayScheduledTransaction(String name,ScheduledTransactionTiming timing,DateType dateType, TransactionType transactionType, String amountExpression, Integer sequence){
        ScheduledTransaction scheduledTransaction = new ScheduledTransaction( name,timing,dateType, null, transactionType, amountExpression, sequence);

        scheduledTransactions.add(scheduledTransaction);

        return  scheduledTransaction;
    }

    public ScheduledTransaction addScheduledTransaction(String name,ScheduledTransactionTiming timing,ScheduleType scheduleType, TransactionType transactionType, String amountExpression, Integer sequence){
        ScheduledTransaction scheduledTransaction = new ScheduledTransaction( name,timing,null, scheduleType, transactionType, amountExpression, sequence);

        scheduledTransactions.add(scheduledTransaction);

        return  scheduledTransaction;
    }

    public Function<String,String> currentTimestamp() {
        return (v)-> LocalDateTime.now().toString();
    }

}
