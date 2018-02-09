package com.transactrules.accounts.dynamoDB;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.LocalDate;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@DynamoDBTypeConverted(converter=LocalDateFormat.LocalDateConverter.class)
public @interface LocalDateFormat {


    class LocalDateConverter implements DynamoDBTypeConverter<String, LocalDate> {

        @Override
        public String convert( final LocalDate time ) {

            return time.toString();
        }

        @Override
        public LocalDate unconvert( final String stringValue ) {

            return LocalDate.parse(stringValue);
        }
    }
}
