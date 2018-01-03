package com.transactrules.accounts.utilities;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.transactrules.accounts.config.ObjectMapperConfiguration;
import com.transactrules.accounts.runtime.HolidayDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;


public class HolidayDateListConverter implements DynamoDBTypeConverter<String, List<HolidayDate>> {

    Logger logger = LoggerFactory.getLogger(HolidayDateListConverter.class);

    @Override
    public String convert(List<HolidayDate>objects) {
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
    public List<HolidayDate>unconvert(String objectsString) {
        ObjectMapper objectMapper = ObjectMapperConfiguration.getObjectMapper();
        try {
            List<HolidayDate> objects = objectMapper.readValue(objectsString, new TypeReference<List<HolidayDate>>(){});
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
