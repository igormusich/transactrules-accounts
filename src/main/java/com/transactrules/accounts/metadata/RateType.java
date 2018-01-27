package com.transactrules.accounts.metadata;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.transactrules.accounts.NamedAbstractEntity;


@DynamoDBDocument
public class RateType extends NamedAbstractEntity {
    private Boolean isRequired;

    public RateType(){}

    public RateType(String name, Boolean isRequired) {

        super(name);
        this.isRequired = isRequired;
    }

    @DynamoDBAttribute
    public Boolean getRequired() {
        return isRequired;
    }

    public void setRequired(Boolean required) {
        isRequired = required;
    }


}
