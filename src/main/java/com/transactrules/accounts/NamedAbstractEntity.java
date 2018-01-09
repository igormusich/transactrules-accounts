package com.transactrules.accounts;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.transactrules.accounts.utilities.Utilities;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by Igor Music on 2016/11/11.
 */

@DynamoDBDocument
public abstract class NamedAbstractEntity {

    @NotBlank
    protected String propertyName;

    @NotBlank
    protected String labelName;


    public NamedAbstractEntity()
    {
    }


    public NamedAbstractEntity(String propertyName){
        this.propertyName = propertyName;
        this.labelName = Utilities.splitCamelCase(this.propertyName);
    }

    public NamedAbstractEntity(String propertyName, String labelName) {
        this.propertyName = propertyName;
        this.labelName = labelName;
    }

    @DynamoDBAttribute
    public String getPropertyName(){
        return this.propertyName;
    }

    public void setPropertyName(String propertyName){
        this.propertyName = propertyName;
    }

    @DynamoDBAttribute
    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }
}
