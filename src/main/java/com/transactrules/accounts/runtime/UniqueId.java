package com.transactrules.accounts.runtime;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "UniqueId")
public class UniqueId {
    private String className;
    private Long nextId;
    private Integer size;
    private String prefix;

    public UniqueId() {
    }

    public UniqueId(String className, Long nextId, String prefix, Integer size) {
        this.className = className;
        this.nextId = nextId;
        this.prefix = prefix;
        this.size = size;
    }

    @DynamoDBHashKey
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @DynamoDBAttribute
    public Long getNextId() {
        return nextId;
    }

    public void setNextId(Long nextId) {
        this.nextId = nextId;
    }

    @DynamoDBAttribute
    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @DynamoDBAttribute
    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String allocateNextId(){

        String formattingString = "%s%0" + size.toString() + "d";

        String result = String.format(formattingString, prefix, nextId);
        nextId ++;

        return result;
    }


}
