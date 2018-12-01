package com.transactrules.accounts;

import com.transactrules.accounts.utilities.Utility;
import javax.validation.constraints.NotEmpty;

/**
 * Created by Igor Music on 2016/11/11.
 */

public abstract class NamedAbstractEntity {

    @NotEmpty
    protected String propertyName;

    @NotEmpty
    protected String labelName;


    public NamedAbstractEntity()
    {
    }


    public NamedAbstractEntity(String propertyName){
        this.propertyName = propertyName;
        this.labelName = Utility.splitCamelCase(this.propertyName);
    }

    public NamedAbstractEntity(String propertyName, String labelName) {
        this.propertyName = propertyName;
        this.labelName = labelName;
    }

    public String getPropertyName(){
        return this.propertyName;
    }

    public void setPropertyName(String propertyName){
        this.propertyName = propertyName;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }
}
