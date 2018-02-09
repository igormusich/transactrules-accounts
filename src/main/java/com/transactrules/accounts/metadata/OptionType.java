package com.transactrules.accounts.metadata;

import com.transactrules.accounts.NamedAbstractEntity;


public class OptionType extends NamedAbstractEntity {

    private String optionListExpression;

    private Boolean isRequired;

    public OptionType(){

    }

    public OptionType( String name, String optionListExpression, Boolean isRequired) {
        super(name);
        this.optionListExpression = optionListExpression;
        this.isRequired = isRequired;
    }

    public String getOptionListExpression() {
        return optionListExpression;
    }

    public void setOptionListExpression(String optionListExpression) {
        this.optionListExpression = optionListExpression;
    }

    public Boolean getRequired() {
        return isRequired;
    }

    public void setRequired(Boolean required) {
        isRequired = required;
    }


}
