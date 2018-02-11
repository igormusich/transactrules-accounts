package com.transactrules.accounts.dynamoDB.account;

import com.transactrules.accounts.dynamoDB.account.AccountDataObject;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;


@EnableScan
public interface DynamoAccountRepository extends CrudRepository<AccountDataObject,String> {

}
