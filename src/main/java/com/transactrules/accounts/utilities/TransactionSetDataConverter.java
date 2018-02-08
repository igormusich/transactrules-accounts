package com.transactrules.accounts.utilities;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.transactrules.accounts.config.ObjectMapperConfiguration;
import com.transactrules.accounts.runtime.TransactionSetData;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TransactionSetDataConverter implements DynamoDBTypeConverter<String, TransactionSetData> {
    @Override
    public String convert(TransactionSetData object) {
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
    public TransactionSetData unconvert(String value) {
        ObjectMapper yamlMapper = getObjectMapper();

        TransactionSetData data=null;

        try {
            data= yamlMapper.readValue(value, new TypeReference<TransactionSetData>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    @NotNull
    private ObjectMapper getObjectMapper() {
        ObjectMapper yamlMapper = ObjectMapperConfiguration.getYamlObjectMapper();

        Map<String,String> customMap = new HashMap<>();

        customMap.put("actionDate","ad");
        customMap.put("valueDate","vd");
        customMap.put("amount","a");
        customMap.put("fromDate","fd");
        customMap.put("toDate","td");
        customMap.put("transactionTypeName","tt");
        customMap.put("items","i");
        customMap.put("transactionSetId","id");
        customMap.put("nextTransactionSetId","nid");
        customMap.put("accountNumber","an");
        customMap.put("year","y");
        customMap.put("month","m");
        customMap.put("setId","si");
        customMap.put("count","c");
        customMap.put("list","l");
        customMap.put("repeatableLists","rl");
        customMap.put("positions","p");
        customMap.put("transactionTypeMap","ttm");
        customMap.put("positionTypeMap","ptm");


        yamlMapper.setPropertyNamingStrategy(new CustomNamingStrategy(customMap));
        return yamlMapper;
    }
}
