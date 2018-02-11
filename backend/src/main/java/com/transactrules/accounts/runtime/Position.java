package com.transactrules.accounts.runtime;

import com.transactrules.accounts.metadata.TransactionOperation;

import java.math.BigDecimal;


public class Position  {

    private BigDecimal amount;


    public Position() {
        this.amount = BigDecimal.ZERO;
    }

    public void applyOperation(TransactionOperation operation, BigDecimal value){
        switch (operation) {
            case Subtract:
                amount = amount.subtract(value);
                break;
            case Add:
                amount = amount.add(value);
                break;
            default:
                break;
        }
    }

    public BigDecimal add(BigDecimal value) {
        BigDecimal result = amount.add(value);

        amount = result;

        return amount;
    }

    public BigDecimal subtract(BigDecimal value) {
        BigDecimal result = amount.subtract(value);

        amount = result;

        return amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

}
