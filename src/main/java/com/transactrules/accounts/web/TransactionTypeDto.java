package com.transactrules.accounts.web;

import java.util.ArrayList;
import java.util.List;

public class TransactionTypeDto {
    private String name;
    private boolean maximumPrecision;
    private List<String> creditPositionNames = new ArrayList<>();
    private List<String> debitPositionNames = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isMaximumPrecision() {
        return maximumPrecision;
    }

    public void setMaximumPrecision(boolean maximumPrecision) {
        this.maximumPrecision = maximumPrecision;
    }

    public List<String> getCreditPositionNames() {
        return creditPositionNames;
    }

    public void setCreditPositionNames(List<String> creditPositionNames) {
        this.creditPositionNames = creditPositionNames;
    }

    public List<String> getDebitPositionNames() {
        return debitPositionNames;
    }

    public void setDebitPositionNames(List<String> debitPositionNames) {
        this.debitPositionNames = debitPositionNames;
    }



}
