package com.transactrules.accounts.runtime;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

import java.util.ArrayList;
import java.util.List;


@DynamoDBDocument
public class OptionValue {

    private String value;

    private List<String> values = new ArrayList<>();

    public OptionValue(){

    }

    public OptionValue(String value){
        this.value = value;
    }

    public OptionValue(String value, List<String> values ){
        this.value = value;
        this.values = values;
    }

    @DynamoDBAttribute
    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> list){
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
