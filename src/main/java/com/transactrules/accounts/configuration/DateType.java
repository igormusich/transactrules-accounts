package com.transactrules.accounts.configuration;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.transactrules.accounts.NamedAbstractEntity;


@DynamoDBDocument
public class DateType extends NamedAbstractEntity {


    DateType(){}

    DateType(String name) {
        super(name);
    }

}
