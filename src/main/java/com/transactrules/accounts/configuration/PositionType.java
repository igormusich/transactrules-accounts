package com.transactrules.accounts.configuration;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.transactrules.accounts.NamedAbstractEntity;


/**
 * Define configuration for position type
 */

@DynamoDBDocument
public class PositionType  extends NamedAbstractEntity {

    public PositionType() {

    }

    public PositionType( String name) {
        super(name);
    }

}
