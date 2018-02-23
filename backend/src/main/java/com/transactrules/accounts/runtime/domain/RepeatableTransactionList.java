package com.transactrules.accounts.runtime.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RepeatableTransactionList  {
    private LocalDate fromDate;
    private LocalDate toDate;
    private String transactionTypeName;
    private BigDecimal amount;

    public RepeatableTransactionList() {
    }

    public RepeatableTransactionList(Transaction transaction) {

        if(!transaction.getActionDate().equals(transaction.getValueDate())){
            throw new InvalidParameterException("RepeatableTransactionList can be created only for transactions which have same action and value date");
        }

        this.fromDate = transaction.getActionDate();
        this.toDate = transaction.getActionDate();
        this.transactionTypeName = transaction.getTransactionTypeName();
        this.amount = transaction.getAmount();
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public String getTransactionTypeName() {
        return transactionTypeName;
    }

    public void setTransactionTypeName(String transactionTypeName) {
        this.transactionTypeName = transactionTypeName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @JsonIgnore
    public List<Transaction> getItems() {
        LocalDate iterator = fromDate;
        List<Transaction> items = new ArrayList<>();

        while (iterator.isBefore(toDate) || iterator.isEqual(toDate) )
        {
            items.add(new Transaction(transactionTypeName,amount,iterator,iterator ));

            iterator = iterator.plusDays(1);
        }

        return items;
    }

    public boolean canAdd(Transaction transaction) {
        return (isContinuation(transaction) && hasSameTransactionTypeAndAmount(transaction));
    }

    public void add(Transaction transaction){
        if(!canAdd(transaction)){
            throw new InvalidParameterException("transaction is not continuation of existing series");
        }

        toDate = transaction.getActionDate();
    }

    private boolean isContinuation(Transaction transaction) {
        return transaction.getActionDate().equals((toDate.plusDays(1)));
    }

    private boolean hasSameTransactionTypeAndAmount(Transaction transaction) {
        return transactionTypeName.equals(transaction.getTransactionTypeName()) && amount.equals(transaction.getAmount());
    }


}
