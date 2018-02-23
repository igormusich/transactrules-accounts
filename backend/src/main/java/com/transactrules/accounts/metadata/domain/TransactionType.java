package com.transactrules.accounts.metadata.domain;

import com.transactrules.accounts.NamedAbstractEntity;
import io.swagger.annotations.ApiModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Define metadata for transaction type
 */

@ApiModel
public class TransactionType extends NamedAbstractEntity {

    private boolean maximumPrecision;

    private List<TransactionRuleType> transactionRules = new ArrayList<>();

    public TransactionType(String name,  boolean hasMaximumPrecission) {
        super(name);
        this.maximumPrecision = hasMaximumPrecission;
    }

    public TransactionType(){

    }

    public boolean getMaximumPrecision() {
        return maximumPrecision;
    }

    public void setMaximumPrecision(boolean maximumPrecision) {
        this.maximumPrecision = maximumPrecision;
    }

    public List<TransactionRuleType> getTransactionRules() {

        return transactionRules;
    }

    public void setTransactionRules(List<TransactionRuleType> transactionRules) {
        this.transactionRules = transactionRules;
    }

    public TransactionType addRule(PositionType positionType, TransactionOperation operation) {
        transactionRules.add(new TransactionRuleType(positionType, operation));

        return this;
    }
}
