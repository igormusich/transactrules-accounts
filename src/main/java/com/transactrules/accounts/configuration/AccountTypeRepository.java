package com.transactrules.accounts.configuration;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface AccountTypeRepository extends CrudRepository<AccountType, String> {
}
