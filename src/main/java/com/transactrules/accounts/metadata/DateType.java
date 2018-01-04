package com.transactrules.accounts.metadata;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.transactrules.accounts.NamedAbstractEntity;


@DynamoDBDocument
public class DateType extends NamedAbstractEntity {


    public DateType(){}

    DateType(String name) {

        super(name);
    }

}