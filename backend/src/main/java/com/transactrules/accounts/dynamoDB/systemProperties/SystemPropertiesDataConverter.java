package com.transactrules.accounts.dynamoDB.systemProperties;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.transactrules.accounts.config.ObjectMapperConfiguration;
import com.transactrules.accounts.runtime.SystemProperties;

import java.io.IOException;

public class SystemPropertiesDataConverter implements DynamoDBTypeConverter<String, SystemProperties> {
    @Override
    public String convert(SystemProperties object) {
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
    public SystemProperties unconvert(String value) {
        ObjectMapper yamlMapper = getObjectMapper();

        SystemProperties data=null;

        try {
            data= yamlMapper.readValue(value, new TypeReference<SystemProperties>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    private ObjectMapper getObjectMapper() {
        ObjectMapper yamlMapper = ObjectMapperConfiguration.getYamlObjectMapper();

        return yamlMapper;
    }
}
