package com.transactrules.accounts.runtime.persistence.account;


import com.fasterxml.jackson.core.type.TypeReference;
import com.transactrules.accounts.config.ObjectMapperConfiguration;
import com.transactrules.accounts.runtime.domain.Account;
import com.transactrules.accounts.utilities.Utility;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.io.IOException;

@Entity()
@Table(name="Account")
public class AccountDataObject {
    @NotBlank
    private String accountNumber;

    @NotBlank
    private String data;

    public AccountDataObject(){}

    public AccountDataObject(Account account){
        this.accountNumber = account.getAccountNumber();
        setAccount(account);
    }

    @Id
    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @Lob
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Transient
    public Account getAccount() {

        Account object = null;
        try {
            object =  ObjectMapperConfiguration.getYamlObjectMapper().readValue(data, new TypeReference<Account>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }

        return object;

    }

    public void setAccount(Account account) {

        this.data = Utility.getYaml(account);
    }
}
