package com.transactrules.accounts.runtime;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;


@EnableScan
public interface TransactionRepository extends CrudRepository<Transaction,String> {
    //List<Transaction> findByAccountNumberAndActionDateBetween(String accountNumber, LocalDate fromActionDate, LocalDate toActionDate);
}
