package com.transactrules.accounts.persistence.systemProperties;

import com.fasterxml.jackson.core.type.TypeReference;
import com.transactrules.accounts.config.ObjectMapperConfiguration;
import com.transactrules.accounts.runtime.Account;
import com.transactrules.accounts.runtime.SystemProperties;
import com.transactrules.accounts.utilities.Utility;

import javax.persistence.*;
import java.io.IOException;

@Entity
@Table(name="SystemProperties")
public class SystemPropertiesDataObject {
    private String id;

    private String data;

    public SystemPropertiesDataObject(){

    }

    public SystemPropertiesDataObject(SystemProperties data) {
        this.id = data.getId();
        setProperties(data);
    }

    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Transient
    public SystemProperties getProperties() {

        SystemProperties object = null;
        try {
            object =  ObjectMapperConfiguration.getYamlObjectMapper().readValue(data, new TypeReference<SystemProperties>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }

        return object;
    }

    public void setProperties(SystemProperties data) {

        this.data = Utility.getYaml(data);
    }

    @Lob
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

}
