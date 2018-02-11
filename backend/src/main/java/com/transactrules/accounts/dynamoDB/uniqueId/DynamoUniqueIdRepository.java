package com.transactrules.accounts.dynamoDB.uniqueId;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface DynamoUniqueIdRepository extends CrudRepository<UniqueIdDataObject,String> {

}
