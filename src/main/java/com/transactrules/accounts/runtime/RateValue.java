package com.transactrules.accounts.runtime;


import java.math.BigDecimal;
import java.time.LocalDate;

public class RateValue {
    private BigDecimal value;
    private LocalDate valueDate;

    public RateValue(){

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

/*public class RateValue:Entity
        {
public virtual Entity Consumer { get; set; }
public virtual decimal Value { get; set; }
public virtual DateTime? ValueDate { get; set; }
public virtual string RateType { get; set; }

public static implicit operator decimal(RateValue rateValue)
        {
        return rateValue.Value;
        }
        }*/
