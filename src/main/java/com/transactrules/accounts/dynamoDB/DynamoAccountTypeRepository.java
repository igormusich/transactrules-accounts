package com.transactrules.accounts.dynamoDB;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface DynamoAccountTypeRepository extends CrudRepository<AccountTypeDataObject, String> {

}
