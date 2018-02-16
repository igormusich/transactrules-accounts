package com.transactrules.accounts.utilities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.transactrules.accounts.config.ObjectMapperConfiguration;
import com.transactrules.accounts.runtime.Account;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utility {

    public static String getYaml(Object obj){
        String yaml = "";

        try {
            yaml = ObjectMapperConfiguration.getYamlObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return yaml;
    }

    public static <T> T  getObject(String value){
        T object = null;
        try {
            object =  ObjectMapperConfiguration.getYamlObjectMapper().readValue(value, new TypeReference<T>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }

        return object;
    }

    public static BigInteger checksum(Object obj) throws IOException, NoSuchAlgorithmException {

        if (obj == null) {
            return BigInteger.ZERO;
        }


        YAMLFactory yf = new YAMLFactory();
        ObjectMapper mapper = new ObjectMapper(yf);

        String yamlString = mapper.writeValueAsString(obj);

        MessageDigest m = MessageDigest.getInstance("SHA1");
        m.update(yamlString.getBytes());

        return new BigInteger(1, m.digest());
    }

    public static String splitCamelCase(String s) {
        return s.replaceAll(
                String.format("%s|%s|%s",
                        "(?<=[A-Z])(?=[A-Z][a-z])",
                        "(?<=[^A-Z])(?=[A-Z])",
                        "(?<=[A-Za-z])(?=[^A-Za-z])"
                ),
                " "
        );
    }

    public static String getIdentifier(String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            if (Character.isJavaIdentifierStart(str.charAt(0)) || i > 0 && Character.isJavaIdentifierPart(str.charAt(i)))
                sb.append(str.charAt(i));
            else
                sb.append((int)str.charAt(i));
        }
        return sb.toString();
    }

    public static boolean isEmpty(String input) {
        return (input == null || input.isEmpty() );
    }



}
