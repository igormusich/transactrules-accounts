package com.transactrules.accounts.configuration;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * Created by 313798977 on 2016/11/12.
 */

@DynamoDBDocument
public class TransactionRuleType  {

    private String posititonTypeId;

    private int transactionOperation;

    public TransactionRuleType()
    {

    }

    public TransactionRuleType(PositionType posititonType, TransactionOperation operation) {
        this.posititonTypeId = posititonType.getId();
        this.transactionOperation = operation.value();
    }

    public String getPosititonTypeId() {
        return posititonTypeId;
    }

    public void setPosititonTypeId(String posititonTypeId) {
        this.posititonTypeId = posititonTypeId;
    }

    public int getTransactionOperation() {
        return transactionOperation;
    }

    public void setTransactionOperation(int transactionOperation) {
        this.transactionOperation = transactionOperation;
    }

}
