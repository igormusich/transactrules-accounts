package com.transactrules.accounts.repository;

import com.transactrules.accounts.metadata.AccountType;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface AccountTypeRepository extends CrudRepository<AccountType, String> {

}
