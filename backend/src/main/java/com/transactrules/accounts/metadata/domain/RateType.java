package com.transactrules.accounts.metadata.domain;

import com.transactrules.accounts.NamedAbstractEntity;

public class RateType extends NamedAbstractEntity {
    private Boolean isRequired;

    public RateType(){}

    public RateType(String name, Boolean isRequired) {

        super(name);
        this.isRequired = isRequired;
    }

    public Boolean getRequired() {
        return isRequired;
    }

    public void setRequired(Boolean required) {
        isRequired = required;
    }


}
