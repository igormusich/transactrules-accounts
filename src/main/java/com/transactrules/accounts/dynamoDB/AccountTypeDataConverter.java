package com.transactrules.accounts.dynamoDB;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.transactrules.accounts.config.ObjectMapperConfiguration;
import com.transactrules.accounts.metadata.AccountType;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class AccountTypeDataConverter implements DynamoDBTypeConverter<String, AccountType> {
    @Override
    public String convert(AccountType object) {
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
    public AccountType unconvert(String value) {
        ObjectMapper yamlMapper = getObjectMapper();

        AccountType data=null;

        try {
            data= yamlMapper.readValue(value, new TypeReference<AccountType>(){});
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
