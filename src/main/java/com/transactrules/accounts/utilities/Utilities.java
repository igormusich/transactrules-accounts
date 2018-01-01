package com.transactrules.accounts.utilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utilities {
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
}
