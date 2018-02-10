package com.transactrules.accounts.dynamoDB.accountType;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.transactrules.accounts.metadata.AccountType;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Aggregate root for account metadata
 */

@DynamoDBTable(tableName = "AccountType")
public class AccountTypeDataObject {

    @NotBlank
    private String className;

    @NotBlank
    private AccountType accountType;

    public AccountTypeDataObject(){

    }

    public AccountTypeDataObject(AccountType accountType){
        this.className = accountType.getClassName();
        this.accountType = accountType;
    }

    @DynamoDBHashKey
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @DynamoDBTypeConverted(converter = AccountTypeDataConverter.class)
    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
}
