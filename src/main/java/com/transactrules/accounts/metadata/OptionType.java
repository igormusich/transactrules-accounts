package com.transactrules.accounts.metadata;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.transactrules.accounts.NamedAbstractEntity;


@DynamoDBDocument
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

    @DynamoDBAttribute
    public String getOptionListExpression() {
        return optionListExpression;
    }

    public void setOptionListExpression(String optionListExpression) {
        this.optionListExpression = optionListExpression;
    }

    @DynamoDBAttribute
    public Boolean getRequired() {
        return isRequired;
    }

    public void setRequired(Boolean required) {
        isRequired = required;
    }


}
