package com.transactrules.accounts.metadata;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.transactrules.accounts.NamedAbstractEntity;
import io.swagger.annotations.ApiModel;

@DynamoDBDocument
@ApiModel
public class AmountType extends NamedAbstractEntity {

    private Boolean isValueDated;
    private Boolean isRequired;

    public AmountType(){}

    public AmountType(String name, Boolean isValueDated,Boolean isRequired) {
        super(name);
        this.isValueDated = isValueDated;
        this.isRequired = isRequired;
    }

    @DynamoDBAttribute
    public Boolean getValueDated() {
        return isValueDated;
    }

    public void setValueDated(Boolean valueDated) {
        isValueDated = valueDated;
    }

    @DynamoDBAttribute
    public Boolean getRequired() {
        return isRequired;
    }

    public void setRequired(Boolean required) {
        isRequired = required;
    }
}
