package com.transactrules.accounts.dynamoDB.systemProperties;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;


@EnableScan
public interface DynamoSystemPropertiesRepository extends CrudRepository<SystemPropertiesDataObject,String> {

}
