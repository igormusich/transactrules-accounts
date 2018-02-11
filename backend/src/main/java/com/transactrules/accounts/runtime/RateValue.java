package com.transactrules.accounts.runtime;


import java.math.BigDecimal;
import java.time.LocalDate;

public class RateValue {
    private BigDecimal value;
    private LocalDate valueDate;

    public RateValue(){

    }

    public RateValue(BigDecimal value, LocalDate valueDate) {
        this.value = value;
        this.valueDate = valueDate;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public LocalDate getValueDate() {
        return valueDate;
    }

    public void setValueDate(LocalDate valueDate) {
        this.valueDate = valueDate;
    }
}
