package com.transactrules.accounts;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.*;
import com.transactrules.accounts.configuration.AccountType;
import com.transactrules.accounts.runtime.Account;
import com.transactrules.accounts.runtime.Calendar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StartupApplicationRunner implements ApplicationRunner {

    @Autowired
    private AmazonDynamoDB amazonDynamoDB;

    @Autowired
    private TestConfiguration testConfiguration;

    @Value("${reset-tables}")
    private Boolean resetTables =false;

    private Logger logger = LoggerFactory.getLogger(StartupApplicationRunner.class);

    @Override
    public void run(ApplicationArguments args) throws Exception {
        DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);

        String[] tableNames = {"AccountType","Account","Calendar"};

        List<String> listTablesResult = amazonDynamoDB.listTables().getTableNames();

        if(resetTables){
            for (String tableName: tableNames) {
                if(contains(listTablesResult, tableName)){
                    amazonDynamoDB.deleteTable(tableName);
                    waitForTableToBeDeleted(tableName,amazonDynamoDB);
                }
            }
            listTablesResult = amazonDynamoDB.listTables().getTableNames();
        }

        if(!contains(listTablesResult,"AccountType")) {
            CreateTable(AccountType.class, dynamoDBMapper, amazonDynamoDB);
        }

        if(!contains(listTablesResult,"Account")) {
            CreateTable(Account.class, dynamoDBMapper, amazonDynamoDB);
        }

        if(!contains(listTablesResult,"Calendar")) {
            CreateTable(Calendar.class, dynamoDBMapper, amazonDynamoDB);
        }

        testConfiguration.run();
    }

    private boolean contains(List<String> listTablesResult, String tableName) {
        return listTablesResult.stream().anyMatch(t-> t.equalsIgnoreCase(tableName));
    }

    public void CreateTable(Class<?> clazz, DynamoDBMapper dynamoDBMapper, AmazonDynamoDB amazonDynamoDB){
        CreateTableRequest tableRequest = dynamoDBMapper
                .generateCreateTableRequest(clazz);
        tableRequest.setProvisionedThroughput(
                new ProvisionedThroughput(1L, 1L));

        CreateTableResult result= amazonDynamoDB.createTable(tableRequest);

        try {
            waitForTableToBecomeAvailable(tableRequest.getTableName(),amazonDynamoDB);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        logger.info(String.format("create table for class %s", clazz.getName()));
    }

    protected void waitForTableToBecomeAvailable(String tableName, AmazonDynamoDB amazonDynamoDB) throws InterruptedException {
        logger.info("Waiting for " + tableName + " to become ACTIVE...");

        long startTime = System.currentTimeMillis();
        long endTime = startTime + (60 * 1000);
        while ( System.currentTimeMillis() < endTime ) {

            try {
                DescribeTableRequest request = new DescribeTableRequest()
                        .withTableName(tableName);
                TableDescription table = amazonDynamoDB.describeTable(request).getTable();
                if ( table == null ) continue;

                String tableStatus = table.getTableStatus();
                logger.info("  - current state: " + tableStatus);
                if ( tableStatus.equals(TableStatus.ACTIVE.toString()) )
                    return;
            } catch ( AmazonServiceException ase ) {
                if (!ase.getErrorCode().equalsIgnoreCase("ResourceNotFoundException"))
                    throw ase;
            }
            Thread.sleep(200);
        }

        throw new RuntimeException("Table " + tableName + " never went active");
    }

    protected void waitForTableToBeDeleted(String tableName, AmazonDynamoDB amazonDynamoDB) throws InterruptedException {
        logger.info("Waiting for " + tableName + " to be deleted...");

        long startTime = System.currentTimeMillis();
        long endTime = startTime + (60 * 1000);
        while ( System.currentTimeMillis() < endTime ) {

            try {
                DescribeTableRequest request = new DescribeTableRequest()
                        .withTableName(tableName);
                TableDescription table = amazonDynamoDB.describeTable(request).getTable();
                if ( table == null )
                    return;

                String tableStatus = table.getTableStatus();
                logger.info("  - current state: " + tableStatus);
            } catch ( AmazonServiceException ase ) {
                if (!ase.getErrorCode().equalsIgnoreCase("ResourceNotFoundException")) {
                    throw ase;
                } else {
                    return;
                }
            }
            Thread.sleep(200);
        }

        throw new RuntimeException("Table " + tableName + " was never deleted");
    }

}
