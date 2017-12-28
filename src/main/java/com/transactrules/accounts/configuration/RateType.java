package com.transactrules.accounts.configuration;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.transactrules.accounts.NamedAbstractEntity;


@DynamoDBDocument
public class RateType extends NamedAbstractEntity {

    public RateType(){}

    public RateType(String name) {
        super(name);
    }

}
