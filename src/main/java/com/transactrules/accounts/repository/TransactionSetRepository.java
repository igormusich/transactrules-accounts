package com.transactrules.accounts.repository;

import com.transactrules.accounts.runtime.TransactionSet;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;


@EnableScan
public interface TransactionSetRepository extends CrudRepository<TransactionSet,String> {
}
