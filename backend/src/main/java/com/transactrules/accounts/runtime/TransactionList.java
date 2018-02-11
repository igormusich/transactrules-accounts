package com.transactrules.accounts.runtime;

import java.util.ArrayList;
import java.util.List;

public class TransactionList  {
    public TransactionList() {
    }

    public TransactionList(List<Transaction> items) {
        this.items = items;
    }

    private List<Transaction> items = new ArrayList<>();

    public List<Transaction> getItems() {
        return items;
    }

    public void setItems(List<Transaction> items) {
        this.items = items;
    }
}
