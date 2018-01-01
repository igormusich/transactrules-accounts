package com.transactrules.accounts;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by Igor Music on 2016/11/11.
 */

@DynamoDBDocument
public abstract class NamedAbstractEntity {

    @NotEmpty
    protected String name;


    public NamedAbstractEntity()
    {
    }


    public NamedAbstractEntity(String name){

        this.name = name;
    }
    

    @DynamoDBAttribute
    public String getName(){

        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

}
