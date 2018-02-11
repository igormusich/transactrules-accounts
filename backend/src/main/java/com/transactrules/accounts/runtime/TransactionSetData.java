package com.transactrules.accounts.runtime;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionSetData {
    private String accountNumber;
    private Integer year;
    private Integer month;
    private Integer setId;
    private Integer count = 0;
    private TransactionList list = new TransactionList();
    private List<RepeatableTransactionList> repeatableLists = new ArrayList<>();
    private Map<String, String> transactionTypeMap = new HashMap<>();
    private Map<String, String> positionTypeMap = new HashMap<>();

    private static int MAX_CAPACITY = 2000;

    public TransactionSetData(){}

    public TransactionSetData(String accountNumber, Integer year, Integer month, Integer setId) {
        this.accountNumber = accountNumber;
        this.year = year;
        this.month = month;
        this.setId = setId;
    }

    public void addTransaction(Transaction transaction){
        list.getItems().add(pack(transaction));
    }

    @DynamoDBIgnore
    @JsonIgnore
    public List<Transaction> getTransactions(){
        List<Transaction> transactions = new ArrayList<>();

        for(Transaction transaction: list.getItems()){
            transactions.add(unpackTransaction(transaction));
        }

        return transactions;
    }

    private Transaction pack(Transaction transaction){
        if(!transactionTypeMap.containsKey(transaction.getTransactionTypeName())){
            transactionTypeMap.put(transaction.getTransactionTypeName(), String.format("%d" ,transactionTypeMap.keySet().size()));
        }

        for(String positionType: transaction.getPositions().keySet()){
            if(!positionTypeMap.containsKey(positionType)){
                positionTypeMap.put(positionType, String.format("%d" ,positionTypeMap.keySet().size()));
            }
        }

        Transaction packedTransaction = new Transaction( transactionTypeMap.get(transaction.getTransactionTypeName()), transaction.getAmount(),transaction.getActionDate(), transaction.getValueDate());

        Map<String, BigDecimal> packedPositions = new HashMap<>();

        for(String positionType: transaction.getPositions().keySet()){
            packedPositions.put( positionTypeMap.get(positionType), transaction.getPositions().get(positionType));
        }

        packedTransaction.setPositions(packedPositions);

        return packedTransaction;
    }

    private Transaction unpackTransaction(Transaction packed){
        String transactionType = "";

        for(Map.Entry<String,String> entry: transactionTypeMap.entrySet()){
            if (entry.getValue().equals(packed.getTransactionTypeName())) {
                transactionType = entry.getKey();
            }
        }

        Transaction transaction = new Transaction(transactionType, packed.getAmount(), packed.getActionDate(), packed.getValueDate());

        Map<String,BigDecimal> positions = new HashMap<>();

        for(String key: packed.getPositions().keySet()){
            for(Map.Entry<String,String> entry: positionTypeMap.entrySet()){
                if(entry.getValue().equals(key)){
                    positions.put( entry.getKey(), packed.getPositions().get(key));
                }
            }
        }

        transaction.setPositions(positions);

        return transaction;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getSetId() {
        return setId;
    }

    public void setSetId(Integer setId) {
        this.setId = setId;
    }



    public TransactionList getList() {
        return list;
    }

    public void setList(TransactionList list) {
        this.list = list;
    }

    public List<RepeatableTransactionList> getRepeatableLists() {
        return repeatableLists;
    }

    public void setRepeatableLists(List<RepeatableTransactionList> repeatableLists) {
        this.repeatableLists = repeatableLists;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @JsonIgnore
    public Boolean isAtCapacity(){
        return count >= MAX_CAPACITY;
    }


    public void incrementCount() {
        this.count ++;
    }

    public Map<String, String> getTransactionTypeMap() {
        return transactionTypeMap;
    }

    public void setTransactionTypeMap(Map<String, String> transactionTypeMap) {
        this.transactionTypeMap = transactionTypeMap;
    }

    public Map<String, String> getPositionTypeMap() {
        return positionTypeMap;
    }

    public void setPositionTypeMap(Map<String, String> positionTypeMap) {
        this.positionTypeMap = positionTypeMap;
    }
}