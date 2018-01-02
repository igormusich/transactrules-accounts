package com.transactrules.accounts.metadata;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;


/**
 * Created by 313798977 on 2016/11/12.
 */

@DynamoDBDocument
public class TransactionRuleType  {

    private String posititonTypeName;

    private String transactionOperation;

    public TransactionRuleType()
    {

    }

    public TransactionRuleType(PositionType posititonType, TransactionOperation operation) {
        this.posititonTypeName = posititonType.getName();
        this.transactionOperation = operation.value();
    }

    public String getPosititonTypeName() {
        return posititonTypeName;
    }

    public void setPosititonTypeName(String posititonTypeName) {
        this.posititonTypeName = posititonTypeName;
    }

    public String getTransactionOperation() {
        return transactionOperation;
    }

    public void setTransactionOperation(String transactionOperation) {
        this.transactionOperation = transactionOperation;
    }

}
