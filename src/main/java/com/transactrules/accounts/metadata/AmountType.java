package com.transactrules.accounts.metadata;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.transactrules.accounts.NamedAbstractEntity;
import io.swagger.annotations.ApiModel;

@DynamoDBDocument
@ApiModel
public class AmountType extends NamedAbstractEntity {

    private Boolean isValueDated;

    public AmountType(){}

    public AmountType(String name, Boolean isValueDated) {
        super(name);
        this.isValueDated = isValueDated;
    }

    @DynamoDBAttribute
    public Boolean getValueDated() {
        return isValueDated;
    }

    public void setValueDated(Boolean valueDated) {
        isValueDated = valueDated;
    }


}
