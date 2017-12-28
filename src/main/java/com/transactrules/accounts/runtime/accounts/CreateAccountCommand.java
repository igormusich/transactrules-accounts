package com.transactrules.accounts.runtime.accounts;

public class CreateAccountCommand {
    public String accountNumber;
    public Long accountTypeId;

    public CreateAccountCommand(String accountNumber, Long accountTypeId) {
        this.accountNumber = accountNumber;
        this.accountTypeId = accountTypeId;
    }


}
