package com.transactrules.accounts.runtime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

public class TransactionSetData {
    private String accountNumber;
    private Integer year;
    private Integer month;
    private Integer setId;
    private Integer count = 0;
    private TransactionList list = new TransactionList();
    private List<RepeatableTransactionList> repeatableLists = new ArrayList<>();
    private static int MAX_CAPACITY = 2000;

    public TransactionSetData(){}

    public TransactionSetData(String accountNumber, Integer year, Integer month, Integer setId) {
        this.accountNumber = accountNumber;
        this.year = year;
        this.month = month;
        this.setId = setId;

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
}