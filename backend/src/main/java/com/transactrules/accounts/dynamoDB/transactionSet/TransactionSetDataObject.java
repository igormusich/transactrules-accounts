package com.transactrules.accounts.dynamoDB.transactionSet;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.transactrules.accounts.runtime.TransactionSet;

@DynamoDBTable(tableName = "TransactionSet")
public class TransactionSetDataObject {
    // {account number}-{action date yyyy-mm}-{set id}
    private String id;
    private TransactionSet transactionSet;

    public TransactionSetDataObject(){

    }

    public TransactionSetDataObject(TransactionSet transactionSet) {
        this.id = transactionSet.getId();
        this.transactionSet = transactionSet;
    }

    @DynamoDBHashKey
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    @DynamoDBTypeConverted(converter = TransactionSetDataConverter.class)
    public TransactionSet getTransactionSet() {
        return transactionSet;
    }

    public void setTransactionSet(TransactionSet transactionSet) {
        this.transactionSet = transactionSet;
    }
}
