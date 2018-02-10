package com.transactrules.accounts.dynamoDB;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.*;
import com.transactrules.accounts.DatabaseDriver;
import com.transactrules.accounts.dynamoDB.account.AccountDataObject;
import com.transactrules.accounts.dynamoDB.accountType.AccountTypeDataObject;
import com.transactrules.accounts.dynamoDB.calendar.CalendarDataObject;
import com.transactrules.accounts.dynamoDB.systemProperties.SystemPropertiesDataObject;
import com.transactrules.accounts.dynamoDB.transactionSet.TransactionSetDataObject;
import com.transactrules.accounts.dynamoDB.uniqueId.UniqueIdDataObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DynamoDBDriver implements DatabaseDriver {

    @Autowired
    private AmazonDynamoDB amazonDynamoDB;

    @Value("${reset-tables}")
    private Boolean resetTables =false;

    private Logger logger = LoggerFactory.getLogger(DynamoDBDriver.class);


    @Override
    public void generateDataModel() {
        DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);

        String[] tableNames = {"AccountType","Account","Calendar","TransactionSet", "systemProperties", "UniqueId"};

        List<String> listTablesResult = amazonDynamoDB.listTables().getTableNames();

        if(resetTables){
            for (String tableName: tableNames) {
                if(contains(listTablesResult, tableName)){

                    try {
                        amazonDynamoDB.deleteTable(tableName);
                        waitForTableToBeDeleted(tableName,amazonDynamoDB);
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error(e.getMessage());
                    }

                }
            }
            listTablesResult = amazonDynamoDB.listTables().getTableNames();
        }

        if(!contains(listTablesResult,"AccountType")) {
            CreateTable(AccountTypeDataObject.class, dynamoDBMapper, amazonDynamoDB);
        }

        if(!contains(listTablesResult,"Account")) {
            CreateTable(AccountDataObject.class, dynamoDBMapper, amazonDynamoDB);
        }

        if(!contains(listTablesResult,"Calendar")) {
            CreateTable(CalendarDataObject.class, dynamoDBMapper, amazonDynamoDB);
        }

        if(!contains(listTablesResult,"TransactionSet")) {
            CreateTable(TransactionSetDataObject.class, dynamoDBMapper, amazonDynamoDB);
        }

        if(!contains(listTablesResult,"systemProperties")) {
            CreateTable(SystemPropertiesDataObject.class, dynamoDBMapper, amazonDynamoDB);
        }

        if(!contains(listTablesResult,"UniqueId")) {
            CreateTable(UniqueIdDataObject.class, dynamoDBMapper, amazonDynamoDB);
        }
    }

    private boolean contains(List<String> listTablesResult, String tableName) {
        return listTablesResult.stream().anyMatch(t-> t.equalsIgnoreCase(tableName));
    }

    public void CreateTable(Class<?> clazz, DynamoDBMapper dynamoDBMapper, AmazonDynamoDB amazonDynamoDB){
        CreateTableRequest tableRequest = dynamoDBMapper
                .generateCreateTableRequest(clazz);
        tableRequest.setProvisionedThroughput(
                new ProvisionedThroughput(1000L, 1000L));

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
