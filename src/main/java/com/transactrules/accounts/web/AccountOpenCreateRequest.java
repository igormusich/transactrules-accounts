package com.transactrules.accounts.web;

public class AccountOpenCreateRequest {
    public String accountTypeName;
    public String accountNumber;

    public AccountOpenCreateRequest(String accountNumber,String accountTypeName) {
        this.accountTypeName = accountTypeName;
        this.accountNumber = accountNumber;
    }
}
