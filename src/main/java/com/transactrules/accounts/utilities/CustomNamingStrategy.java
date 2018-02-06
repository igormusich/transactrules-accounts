package com.transactrules.accounts.utilities;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;

import java.util.HashMap;
import java.util.Map;

public class CustomNamingStrategy extends PropertyNamingStrategy {

    private Map<String,String> customMap = new HashMap<>();

    public CustomNamingStrategy(Map<String,String> customMap) {
        this.customMap = customMap;
    }

    @Override
    public String nameForGetterMethod(MapperConfig config, AnnotatedMethod method, String defaultName) {

        if(customMap.containsKey(defaultName)){
            return customMap.get(defaultName);
        }

        return defaultName;

    }

    @Override
    public String nameForSetterMethod(MapperConfig config, AnnotatedMethod method, String defaultName) {
        if(customMap.containsKey(defaultName)){
            return customMap.get(defaultName);
        }

        return defaultName;
    }

}
