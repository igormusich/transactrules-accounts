package com.transactrules.accounts.metadata;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.transactrules.accounts.NamedAbstractEntity;


/**
 * Define metadata for position type
 */

@DynamoDBDocument
public class PositionType  extends NamedAbstractEntity {

    private Boolean isPrincipal=false;

    public PositionType() {

    }

    public PositionType( String name) {
        super(name);
    }

    @DynamoDBAttribute
    public Boolean getPrincipal() {
        return isPrincipal;
    }

    public void setPrincipal(Boolean principal) {
        isPrincipal = principal;
    }
}
