package com.transactrules.accounts.repository;

import com.transactrules.accounts.runtime.Account;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;


@EnableScan
public interface AccountRepository extends CrudRepository<Account,String> {

}
