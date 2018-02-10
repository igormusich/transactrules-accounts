package com.transactrules.accounts.dynamoDB.account;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.transactrules.accounts.config.ObjectMapperConfiguration;
import com.transactrules.accounts.runtime.Account;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class AccountDataConverter implements DynamoDBTypeConverter<String, Account> {
    @Override
    public String convert(Account object) {
        ObjectMapper yamlMapper = getObjectMapper();

        String yaml="";
        try {
            yaml = yamlMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return yaml;
    }



    @Override
    public Account unconvert(String value) {
        ObjectMapper yamlMapper = getObjectMapper();

        Account data=null;

        try {
            data= yamlMapper.readValue(value, new TypeReference<Account>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    @NotNull
    private ObjectMapper getObjectMapper() {
        ObjectMapper yamlMapper = ObjectMapperConfiguration.getYamlObjectMapper();

        return yamlMapper;
    }
}
