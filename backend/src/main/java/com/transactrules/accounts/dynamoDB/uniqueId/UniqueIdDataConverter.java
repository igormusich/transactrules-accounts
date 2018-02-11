package com.transactrules.accounts.dynamoDB.uniqueId;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.transactrules.accounts.config.ObjectMapperConfiguration;
import com.transactrules.accounts.runtime.UniqueId;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class UniqueIdDataConverter implements DynamoDBTypeConverter<String, UniqueId> {
    @Override
    public String convert(UniqueId object) {
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
    public UniqueId unconvert(String value) {
        ObjectMapper yamlMapper = getObjectMapper();

        UniqueId data=null;

        try {
            data= yamlMapper.readValue(value, new TypeReference<UniqueId>(){});
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
