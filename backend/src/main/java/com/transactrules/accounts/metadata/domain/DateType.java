package com.transactrules.accounts.metadata.domain;

import com.transactrules.accounts.NamedAbstractEntity;

public class DateType extends NamedAbstractEntity {

    private Boolean isRequired;
    private Boolean isStartDate;

    public DateType(){}

    DateType(String name, Boolean isRequired, Boolean isStartDate) {

        super(name);
        this.isRequired = isRequired;
        this.isStartDate = isStartDate;
    }

    public Boolean getRequired() {
        return isRequired;
    }

    public void setRequired(Boolean required) {
        isRequired = required;
    }

    public Boolean getIsStartDate() {
        return isStartDate;
    }

    public void setIsStartDate(Boolean startDate) {
        isStartDate = startDate;
    }
}
