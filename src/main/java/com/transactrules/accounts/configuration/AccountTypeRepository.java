package com.transactrules.accounts.configuration;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

@EnableScan
public interface AccountTypeRepository extends CrudRepository<AccountType, String> {

    @EnableScan
     List<AccountType> findByName(@Param("name")  String name);
}
