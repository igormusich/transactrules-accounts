package com.transactrules.accounts.metadata.persistence;

import com.fasterxml.jackson.core.type.TypeReference;
import com.transactrules.accounts.config.ObjectMapperConfiguration;
import com.transactrules.accounts.metadata.domain.AccountType;
import com.transactrules.accounts.utilities.Utility;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.IOException;

/**
 * Aggregate root for account metadata
 */

@Entity
@Table(name = "AccountType")
public class AccountTypeDataObject {

    @NotEmpty
    private String className;

    @NotEmpty
    private String data;

    public AccountTypeDataObject(){

    }

    public AccountTypeDataObject(AccountType accountType){
        this.className = accountType.getClassName();
        setAccountType(accountType);
    }

    @Id
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @Lob
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Transient
    public AccountType getAccountType() {

        AccountType object = null;
        try {
            object =  ObjectMapperConfiguration.getYamlObjectMapper().readValue(data, new TypeReference<AccountType>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }

        return object;
    }

    public void setAccountType(AccountType accountType) {

        this.data = Utility.getYaml(accountType);
    }
}
