package com.transactrules.accounts.dynamoDB.calendar;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.transactrules.accounts.config.ObjectMapperConfiguration;
import com.transactrules.accounts.runtime.Calendar;

import java.io.IOException;

public class CalendarDataConverter implements DynamoDBTypeConverter<String, Calendar> {
    @Override
    public String convert(Calendar object) {
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
    public Calendar unconvert(String value) {
        ObjectMapper yamlMapper = getObjectMapper();

        Calendar data=null;

        try {
            data= yamlMapper.readValue(value, new TypeReference<Calendar>(){});
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
