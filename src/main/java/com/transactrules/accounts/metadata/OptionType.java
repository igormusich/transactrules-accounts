package com.transactrules.accounts.metadata;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.transactrules.accounts.NamedAbstractEntity;


@DynamoDBDocument
public class OptionType extends NamedAbstractEntity {

    private String optionListExpression;

    public OptionType(){

    }

    public OptionType( String name, String optionListExpression) {
        super(name);
        this.optionListExpression = optionListExpression;
    }

    @DynamoDBAttribute
    public String getOptionListExpression() {
        return optionListExpression;
    }

    public void setOptionListExpression(String optionListExpression) {
        this.optionListExpression = optionListExpression;
    }

}
