package com.transactrules.accounts.dynamoDB.transactionSet;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;


@EnableScan
public interface DynamoTransactionSetRepository extends CrudRepository<TransactionSetDataObject,String> {
}
