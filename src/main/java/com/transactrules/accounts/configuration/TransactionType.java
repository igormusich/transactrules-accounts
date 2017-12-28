package com.transactrules.accounts.configuration;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.transactrules.accounts.NamedAbstractEntity;

import java.util.*;

/**
 * Define configuration for transaction type
 */

@DynamoDBDocument
public class TransactionType extends NamedAbstractEntity {


    private boolean maximumPrecision;

    private List<TransactionRuleType> transactionRules = new ArrayList<>();

    public TransactionType(String name,  boolean hasMaximumPrecission) {
        super(name);
        this.maximumPrecision = hasMaximumPrecission;
    }

    public TransactionType(){

    }


    @DynamoDBAttribute
    public boolean getMaximumPrecision() {
        return maximumPrecision;
    }

    public void setMaximumPrecision(boolean maximumPrecision) {
        this.maximumPrecision = maximumPrecision;
    }

    @DynamoDBAttribute
    public List<TransactionRuleType> getTransactionRules() {

        return transactionRules;
    }

    public void setTransactionRules(List<TransactionRuleType> transactionRules) {
        this.transactionRules = transactionRules;
    }

    public TransactionType addRule(PositionType positionType, TransactionOperation operation) {
        transactionRules.add(new TransactionRuleType(positionType, operation));

        return this;
    }
}
