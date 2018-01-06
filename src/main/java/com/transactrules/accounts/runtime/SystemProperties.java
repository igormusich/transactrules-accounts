package com.transactrules.accounts.runtime;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.transactrules.accounts.utilities.LocalDateFormat;

import java.time.LocalDate;
import java.util.UUID;

@DynamoDBTable(tableName = "SystemProperties")
public class SystemProperties {
    private String id;
    private LocalDate actionDate;

    public SystemProperties(){
        this.id = UUID.randomUUID().toString();
        this.actionDate = LocalDate.now();
    }

    public SystemProperties(String id, LocalDate actionDate) {
        this.id = id;
        this.actionDate = actionDate;
    }

    @DynamoDBHashKey
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @LocalDateFormat
    public LocalDate getActionDate() {
        return actionDate;
    }

    public void setActionDate(LocalDate actionDate) {
        this.actionDate = actionDate;
    }
}
