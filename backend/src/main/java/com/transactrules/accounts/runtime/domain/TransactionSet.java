package com.transactrules.accounts.runtime.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.transactrules.accounts.metadata.domain.AccountType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TransactionSet {
    // {account number}-{action date yyyy-mm}-{set id}
    private String id;
    private String nextId;
    private String previousId;
    private TransactionSetData data;

    public TransactionSet(){

    }


    public TransactionSet(String accountNumber, LocalDate actionDate, Integer setId) {
        this.id = generateId(accountNumber,actionDate,setId);
        this.data = new TransactionSetData(accountNumber,actionDate.getYear(),actionDate.getMonthValue(),setId);
    }

    public static String generateId(String accountNumber, LocalDate actionDate, Integer setId){
       return String.format("%s-%04d-%02d-%03d", accountNumber, actionDate.getYear(),actionDate.getMonthValue(),setId);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNextId() {
        return nextId;
    }

    public void setNextId(String nextId) {
        this.nextId = nextId;
    }

    public String getPreviousId() {
        return previousId;
    }

    public void setPreviousId(String previousId) {
        this.previousId = previousId;
    }

    public TransactionSetData getData() {
        return data;
    }

    public void setData(TransactionSetData data) {
        this.data = data;
    }



    public boolean listExistsAndAmountAndDateMatch(Transaction transaction, Optional<RepeatableTransactionList> repeatableList) {
        return repeatableList.isPresent() && repeatableList.get().canAdd(transaction);
    }

    public boolean isRepeatable(AccountType accountType, Transaction transaction) {
        return hasSameActionAndValueDate(transaction) && isMaximumPrecision(accountType, transaction);
    }

    public boolean belongsToSet(Transaction transaction) {
        return transaction.getActionDate().getYear() == data.getYear() && transaction.getActionDate().getMonthValue() == data.getMonth();
    }

    public Optional<RepeatableTransactionList> getExistingRepeatableList(Transaction transaction) {

        return data.getRepeatableLists().stream().filter(item -> item.canAdd(transaction)).findFirst();
    }

    private boolean isMaximumPrecision(AccountType accountType, Transaction transaction) {
        return accountType.getTransactionType(transaction.getTransactionTypeName()).get().getMaximumPrecision();
    }

    private boolean hasSameActionAndValueDate(Transaction transaction) {
        return transaction.getActionDate().isEqual(transaction.getValueDate());
    }

    @JsonIgnore
    public List<Transaction> getTransactions() {
        List<Transaction> transactions = new ArrayList<>();


        for (Transaction item : data.getTransactions()) {

            Transaction transaction= new Transaction(item.getTransactionTypeName(), item.getAmount(), item.getActionDate(), item.getValueDate());
            transaction.setPositions(item.getPositions());
            transactions.add(transaction);
        }

        for (RepeatableTransactionList list : data.getRepeatableLists()) {
            for (Transaction item : list.getItems()) {
                transactions.add(new Transaction(item.getTransactionTypeName(), item.getAmount(), item.getActionDate(), item.getValueDate()));
            }
        }

        return transactions;
    }
}
