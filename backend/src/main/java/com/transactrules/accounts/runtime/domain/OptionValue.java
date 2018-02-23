package com.transactrules.accounts.runtime.domain;


import java.util.ArrayList;
import java.util.List;


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

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> list){
        values = list;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
