package com.transactrules.accounts.utilities;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.transactrules.accounts.config.ObjectMapperConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;


public class PositionMapConverter implements DynamoDBTypeConverter<String, Map<String,BigDecimal>> {

    Logger logger = LoggerFactory.getLogger(PositionMapConverter.class);

    @Override
    public String convert(Map<String,BigDecimal>objects) {
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
    public Map<String,BigDecimal>unconvert(String objectsString) {
        ObjectMapper objectMapper = ObjectMapperConfiguration.getObjectMapper();
        try {
            Map<String,BigDecimal> objects = objectMapper.readValue(objectsString, new TypeReference<Map<String,BigDecimal>>(){});
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
