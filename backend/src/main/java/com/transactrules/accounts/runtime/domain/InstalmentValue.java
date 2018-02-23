package com.transactrules.accounts.runtime.domain;

import java.math.BigDecimal;

public class InstalmentValue {
    private BigDecimal amount;
    private Boolean hasFixedValue;

    public InstalmentValue() {
    }

    public InstalmentValue(BigDecimal amount, Boolean hasFixedValue) {
        this.amount = amount;
        this.hasFixedValue = hasFixedValue;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Boolean getHasFixedValue() {
        return hasFixedValue;
    }

    public void setHasFixedValue(Boolean hasFixedValue) {
        this.hasFixedValue = hasFixedValue;
    }
}
