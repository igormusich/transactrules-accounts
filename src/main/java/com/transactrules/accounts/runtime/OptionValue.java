package com.transactrules.accounts.runtime;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.transactrules.accounts.runtime.accounts.Account;


@DynamoDBDocument
public class OptionValue {

    private String value;

    private Iterable<String> values;

    public OptionValue(){

    }

    public Iterable<String> getValues() {
        return values;
    }

    public void setValues(Iterable<String> list){
        values = list;
    }

    @DynamoDBAttribute
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
