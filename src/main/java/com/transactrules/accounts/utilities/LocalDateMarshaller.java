/*
package com.transactrules.accounts.utilities;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMarshaller;

import java.time.LocalDate;

public class LocalDateMarshaller implements DynamoDBMarshaller<LocalDate> {

    @Override
    public String marshall(LocalDate time) {
        return time.toString();
    }

    @Override
    public LocalDate unmarshall(Class<LocalDate> dimensionType, String stringValue) {
        return LocalDate.parse(stringValue);
    }
}
*/
