package com.transactrules.accounts.runtime;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;


@EnableScan
public interface AccountRepository extends CrudRepository<Account,String> {

}
