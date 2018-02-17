package com.transactrules.accounts.persistence.uniqueId;

import com.fasterxml.jackson.core.type.TypeReference;
import com.transactrules.accounts.config.ObjectMapperConfiguration;
import com.transactrules.accounts.runtime.Account;
import com.transactrules.accounts.runtime.UniqueId;
import com.transactrules.accounts.utilities.Utility;

import javax.persistence.*;
import java.io.IOException;

@Entity
@Table(name = "UniqueId")
public class UniqueIdDataObject {
    private String className;
    private String data;

    public UniqueIdDataObject() {
    }

    public UniqueIdDataObject(UniqueId object){
        this.className = object.getClassName();
        setUniqueId(object);
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
    public UniqueId getUniqueId() {
        UniqueId object = null;
        try {
            object =  ObjectMapperConfiguration.getYamlObjectMapper().readValue(data, new TypeReference<UniqueId>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }

        return object;
    }

    public void setUniqueId(UniqueId object) {

        this.data = Utility.getYaml(object);
    }
}
