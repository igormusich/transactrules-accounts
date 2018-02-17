package com.transactrules.accounts.persistence.transactionSet;

import com.fasterxml.jackson.core.type.TypeReference;
import com.transactrules.accounts.config.ObjectMapperConfiguration;
import com.transactrules.accounts.runtime.Account;
import com.transactrules.accounts.runtime.TransactionSet;
import com.transactrules.accounts.utilities.Utility;

import javax.persistence.*;
import java.io.IOException;

@Entity
@Table(name = "TransactionSet")
public class TransactionSetDataObject {
    // {account number}-{action data yyyy-mm}-{set id}
    private String id;
    private String data;

    public TransactionSetDataObject(){

    }

    public TransactionSetDataObject(TransactionSet transactionSet) {
        this.id = transactionSet.getId();
        setTransactionSet(transactionSet);
    }

    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Lob
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Transient
    public TransactionSet getTransactionSet() {

        TransactionSet object = null;
        try {
            object =  ObjectMapperConfiguration.getYamlObjectMapper().readValue(data, new TypeReference<TransactionSet>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }

        return object;
    }

    public void setTransactionSet(TransactionSet transactionSet) {
        this.data = Utility.getYaml(transactionSet);
    }
}
