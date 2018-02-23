package com.transactrules.accounts.runtime.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AmountValue {

    private BigDecimal amount;

    private LocalDate valueDate;

    public AmountValue (){

    }

    public AmountValue(BigDecimal value){
        this.amount = value;
    }

    public AmountValue(BigDecimal value, LocalDate valueDate) {

        this.amount = value;
        this.valueDate = valueDate;
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
}
