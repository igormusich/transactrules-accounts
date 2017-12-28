package com.transactrules.accounts;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.transactrules.accounts.configuration.AccountType;
import com.transactrules.accounts.runtime.Calendar;
import com.transactrules.accounts.runtime.accounts.Account;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;

import java.util.List;

@Component
public class StartupApplicationRunner implements ApplicationRunner {

    @Autowired
    private AmazonDynamoDB amazonDynamoDB;

    @Autowired
    private TestConfiguration testConfiguration;

    private Logger logger = LoggerFactory.getLogger(StartupApplicationRunner.class);

    @Override
    public void run(ApplicationArguments args) throws Exception {
        DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);

        String[] tableNames = {"AccountType","Account","Calendar"};

        List<String> listTablesResult = amazonDynamoDB.listTables().getTableNames();

        for (String tableName: tableNames) {
            if(listTablesResult.stream().anyMatch(t-> t.equalsIgnoreCase(tableName))){
                amazonDynamoDB.deleteTable(tableName);
            }
        }

        CreateTable(AccountType.class, dynamoDBMapper, amazonDynamoDB);
        CreateTable(Account.class, dynamoDBMapper, amazonDynamoDB);
        CreateTable(Calendar.class, dynamoDBMapper, amazonDynamoDB);

        testConfiguration.run();
    }

    public void CreateTable(Class<?> clazz, DynamoDBMapper dynamoDBMapper, AmazonDynamoDB amazonDynamoDB){
        CreateTableRequest tableRequest = dynamoDBMapper
                .generateCreateTableRequest(clazz);
        tableRequest.setProvisionedThroughput(
                new ProvisionedThroughput(1L, 1L));

        amazonDynamoDB.createTable(tableRequest);

        logger.info(String.format("create table for class %s", clazz.getName()));
    }



}
