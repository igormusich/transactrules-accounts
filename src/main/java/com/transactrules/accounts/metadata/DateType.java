package com.transactrules.accounts.metadata;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.transactrules.accounts.NamedAbstractEntity;


@DynamoDBDocument
public class DateType extends NamedAbstractEntity {

    private Boolean isRequired;
    private Boolean isStartDate;

    public DateType(){}

    DateType(String name, Boolean isRequired, Boolean isStartDate) {

        super(name);
        this.isRequired = isRequired;
        this.isStartDate = isStartDate;
    }

    @DynamoDBAttribute
    public Boolean getRequired() {
        return isRequired;
    }

    public void setRequired(Boolean required) {
        isRequired = required;
    }

    @DynamoDBAttribute
    public Boolean getIsStartDate() {
        return isStartDate;
    }

    public void setIsStartDate(Boolean startDate) {
        isStartDate = startDate;
    }
}
