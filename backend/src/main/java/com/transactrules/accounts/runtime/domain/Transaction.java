package com.transactrules.accounts.runtime.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 313798977 on 2016/11/11.
 */


public class Transaction  {

    private String transactionTypeName;
    private BigDecimal amount;
    private LocalDate actionDate;
    private LocalDate valueDate;
    private Map<String,BigDecimal> positions = new HashMap<>();

    public Transaction(){

    }

    public Transaction(String transactionTypeName, BigDecimal amount,  LocalDate actionDate, LocalDate valueDate) {

        this.transactionTypeName = transactionTypeName;
        this.amount = amount;
        this.actionDate = actionDate;
        this.valueDate = valueDate;
    }


    public LocalDate getActionDate() {
        return actionDate;
    }

    public void setActionDate(LocalDate actionDate) {
        this.actionDate = actionDate;
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

    public LocalDate getValueDate() {
        return valueDate;
    }

    public void setValueDate(LocalDate valueDate) {
        this.valueDate = valueDate;
    }

    public Map<String, BigDecimal> getPositions() {
        return positions;
    }

    public void setPositions(Map<String, BigDecimal> positions) {
        this.positions = positions;
    }
}
