package com.transactrules.accounts.dynamoDB;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.transactrules.accounts.config.ObjectMapperConfiguration;
import com.transactrules.accounts.runtime.InstalmentSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;


public class InstalmentSetConverter implements DynamoDBTypeConverter<String, Map<String, InstalmentSet>> {

    Logger logger = LoggerFactory.getLogger(InstalmentSetConverter.class);

    @Override
    public String convert(Map<String, InstalmentSet> objects) {
        //Jackson object mapper
        ObjectMapper objectMapper = ObjectMapperConfiguration.getObjectMapper();

        try {
            String objectsString = objectMapper.writeValueAsString(objects);
            return objectsString;
        } catch (JsonProcessingException e) {
            //do something
        }
        return null;
    }

    @Override
    public Map<String, InstalmentSet> unconvert(String objectsString) {
        ObjectMapper objectMapper = ObjectMapperConfiguration.getObjectMapper();
        try {
            Map<String, InstalmentSet> objects = objectMapper.readValue(objectsString, new TypeReference<Map<String, InstalmentSet>>(){});
            return objects;
        } catch (JsonParseException e) {
            logger.error(e.toString());
        } catch (JsonMappingException e) {
            logger.error(e.toString());
        } catch (IOException e) {
            logger.error(e.toString());
        }
        return null;
    }
}
