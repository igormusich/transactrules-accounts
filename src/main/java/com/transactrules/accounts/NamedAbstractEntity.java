package com.transactrules.accounts;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.UUID;

/**
 * Created by Igor Music on 2016/11/11.
 */

@DynamoDBDocument
public abstract class NamedAbstractEntity {

    @NotEmpty
    protected String name;
    protected String id;

    public NamedAbstractEntity()
    {
    }


    public NamedAbstractEntity(String name){
        this.id = UUID.randomUUID().toString();
        this.name = name;
    }

    @DynamoDBAttribute
    public String getId(){
        return this.id;
    }

    public void setId(String id){
        this.id = id;
    }

    @DynamoDBAttribute
    public String getName(){

        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

}
