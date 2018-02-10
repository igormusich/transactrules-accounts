package com.transactrules.accounts.dynamoDB.account;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.transactrules.accounts.runtime.Account;
import org.hibernate.validator.constraints.NotBlank;

@DynamoDBTable(tableName = "Account")
public class AccountDataObject {
    @NotBlank
    private String accountNumber;

    @NotBlank
    private Account account;

    public AccountDataObject(){}

    public AccountDataObject(Account account){
        this.accountNumber = account.getAccountNumber();
        this.account = account;
    }

    @DynamoDBHashKey
    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @DynamoDBTypeConverted(converter = AccountDataConverter.class)
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
