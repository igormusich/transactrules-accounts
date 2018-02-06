package com.transactrules.accounts.repository;

import com.transactrules.accounts.runtime.UniqueId;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface UniqueIdRepository extends CrudRepository<UniqueId,String> {

}
