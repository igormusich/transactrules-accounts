package com.transactrules.accounts.metadata;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * Created by 313798977 on 2016/11/12.
 */

@ApiModel
public class TransactionRuleType  {

    private String posititonTypeName;

    @ApiModelProperty(dataType = "string", allowableValues = "ADD, SUBTRACT", value = "Transaction Operation", notes = "Controls how does transaction affect specific position")
    private String transactionOperation;

    public TransactionRuleType()
    {

    }

    public TransactionRuleType(PositionType posititonType, TransactionOperation operation) {
        this.posititonTypeName = posititonType.getPropertyName();
        this.transactionOperation = operation.value();
    }

    public String getPositionTypeName() {
        return posititonTypeName;
    }

    public void setPositionTypeName(String posititonTypeName) {
        this.posititonTypeName = posititonTypeName;
    }

    public String getTransactionOperation() {
        return transactionOperation;
    }

    public void setTransactionOperation(String transactionOperation) {
        this.transactionOperation = transactionOperation;
    }

    @JsonIgnore
    public Boolean getAdd(){
        return transactionOperation.equals(TransactionOperation.Add.value());
    }

    @JsonIgnore
    public Boolean getSubtract(){
        return transactionOperation.equals(TransactionOperation.Subtract.value());
    }

}
