package com.transactrules.accounts.dynamoDB.uniqueId;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.transactrules.accounts.runtime.UniqueId;

@DynamoDBTable(tableName = "UniqueId")
public class UniqueIdDataObject {
    private String className;
    private UniqueId object;

    public UniqueIdDataObject() {
    }

    public UniqueIdDataObject(UniqueId object){
        this.className = object.getClassName();
        this.object = object;
    }

    @DynamoDBHashKey
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @DynamoDBTypeConverted(converter = UniqueIdDataConverter.class)
    public UniqueId getObject() {
        return object;
    }

    public void setObject(UniqueId object) {
        this.object = object;
    }
}
