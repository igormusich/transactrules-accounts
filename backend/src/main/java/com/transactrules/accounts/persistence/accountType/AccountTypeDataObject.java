package com.transactrules.accounts.persistence.accountType;

import com.fasterxml.jackson.core.type.TypeReference;
import com.transactrules.accounts.config.ObjectMapperConfiguration;
import com.transactrules.accounts.metadata.AccountType;
import com.transactrules.accounts.utilities.Utility;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.io.IOException;

/**
 * Aggregate root for account metadata
 */

@Entity
@Table(name = "AccountType")
public class AccountTypeDataObject {

    @NotBlank
    private String className;

    @NotBlank
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
