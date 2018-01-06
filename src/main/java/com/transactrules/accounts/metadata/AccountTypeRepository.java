package com.transactrules.accounts.metadata;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface AccountTypeRepository extends CrudRepository<AccountType, String> {

}
