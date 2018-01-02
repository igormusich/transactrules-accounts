package com.transactrules.accounts.metadata;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.transactrules.accounts.NamedAbstractEntity;


/**
 * Define metadata for position type
 */

@DynamoDBDocument
public class PositionType  extends NamedAbstractEntity {

    public PositionType() {

    }

    public PositionType( String name) {
        super(name);
    }

}
