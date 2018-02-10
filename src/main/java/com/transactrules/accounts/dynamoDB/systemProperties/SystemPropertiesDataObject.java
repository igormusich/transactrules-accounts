package com.transactrules.accounts.dynamoDB.systemProperties;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.transactrules.accounts.runtime.SystemProperties;

@DynamoDBTable(tableName = "systemProperties")
public class SystemPropertiesDataObject {
    private String id;
    private SystemProperties data;
    public SystemPropertiesDataObject(){

    }

    public SystemPropertiesDataObject(SystemProperties data) {
        this.id = data.getId();
        this.data = data;
    }

    @DynamoDBHashKey
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @DynamoDBTypeConverted(converter = SystemPropertiesDataConverter.class)
    public SystemProperties getData() {
        return data;
    }

    public void setData(SystemProperties data) {
        this.data = data;
    }
}
